package com.lyj.fakepivix.module.common

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.model.response.IllustListResp
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.data.source.remote.checkEmpty
import com.lyj.fakepivix.app.network.LoadState
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
open class IllustListViewModel(val allowEmpty: Boolean = false, var action: (suspend () -> IllustListResp)? = null) : BaseViewModel<IModel?>() {

    override var mModel: IModel? = null

    val data = ObservableArrayList<Illust>()
    var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)
    var loadMoreState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var nextUrl = ""

    fun load() {
        if (loadMoreState.get() !is LoadState.Loading) {
            action?.let {
                launch(CoroutineExceptionHandler { _, err ->
                    loadState.set(LoadState.Failed(err))
                }) {
                    loadState.set(LoadState.Loading)
                    data.clear()
                    val resp = withContext(Dispatchers.IO) {
                        it()
                    }
                    resp.checkEmpty()
                    nextUrl = resp.next_url
                    data.clear()
                    data.addAll(resp.illusts)
                    loadState.set(LoadState.Succeed)
                }
            }
        }
    }


    fun loadMore() {
        if (nextUrl.isBlank())
            return
        val disposable = IllustRepository.instance
                .loadMore(nextUrl)
                .doOnSubscribe { loadMoreState.set(LoadState.Loading) }
                .subscribeBy(onNext = {
                    loadMoreState.set(LoadState.Succeed)
                    nextUrl = it.next_url
                    data.addAll(it.illusts)
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