package com.lyj.fakepixiv.module.common

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.lyj.fakepixiv.app.base.BaseViewModel

import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.data.model.response.IllustListResp
import com.lyj.fakepixiv.app.data.source.remote.IllustRepository
import com.lyj.fakepixiv.app.data.source.remote.checkEmpty
import com.lyj.fakepixiv.app.network.LoadState
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc
 */
open class IllustListViewModel(var action: (suspend () -> IllustListResp)? = null) : BaseViewModel() {



    val data = ObservableArrayList<Illust>()
    var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)
    var loadMoreState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var nextUrl = ""

    open fun load() {
        //if (loadMoreState.get() !is LoadState.Loading) {
            action?.let {
                launch(CoroutineExceptionHandler { _, err ->
                    loadState.set(LoadState.Failed(err))
                }) {
                    loadState.set(LoadState.Loading)
                    val resp = withContext(Dispatchers.IO) {
                        it()
                    }
                    resp.illusts.filter { it.visible }
                    resp.checkEmpty()
                    nextUrl = resp.next_url
                    loadState.set(LoadState.Succeed)
                    data.clear()
                    data.addAll(resp.illusts)
                }
            }
        //}
    }


    open fun loadMore() {
        if (nextUrl.isBlank())
            return
        val disposable = IllustRepository.instance
                .loadMore(nextUrl)
                .doOnSubscribe { loadMoreState.set(LoadState.Loading) }
                .subscribeBy(onNext = {
                    nextUrl = it.next_url
                    it.illusts.filter { res -> res.visible }
                    data.addAll(it.illusts)
                    loadMoreState.set(LoadState.Succeed)
                }, onError = {
                    loadMoreState.set(LoadState.Failed(it))
                })
        addDisposable(disposable)
    }

    fun clear() {
        data.clear()
        nextUrl = ""
    }
}