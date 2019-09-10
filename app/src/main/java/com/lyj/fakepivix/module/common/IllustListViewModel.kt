package com.lyj.fakepivix.module.common

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel

import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.model.response.IllustListResp
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.network.LoadState
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy

/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc
 */
open class IllustListViewModel(var action: () -> Observable<IllustListResp>) : BaseViewModel<IModel?>() {

    override var mModel: IModel? = null

    val data = ObservableArrayList<Illust>()
    var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)
    var loadMoreState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var nextUrl = ""

    fun load() {
        if (loadMoreState.get() !is LoadState.Loading) {
            val disposable = action()
                    .doOnSubscribe {
                        loadState.set(LoadState.Loading)
                        data.clear()
                    }
                    .subscribeBy(onNext = {
                        loadState.set(LoadState.Succeed)
                        nextUrl = it.next_url
                        data.clear()
                        data.addAll(it.illusts)
                    }, onError = {
                        loadState.set(LoadState.Failed(it))
                    })
            addDisposable(disposable)
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