package com.lyj.fakepivix.module.main.common

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.constant.ILLUST
import com.lyj.fakepivix.app.constant.IllustCategory
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
abstract class IllustListViewModel(@IllustCategory var category: String = ILLUST) : BaseViewModel<IModel?>() {

    override var mModel: IModel? = null

    val data = ObservableArrayList<Illust>()
    var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)
    var loadMoreState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var nextUrl = ""

    fun load() {
        val disposable = getIllustList()
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

    fun loadMore() {
        if (loadMoreState.get() !is LoadState.Loading) {
            val disposable = IllustRepository.instance
                    .loadMore(nextUrl, category)
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
    }

    abstract fun getIllustList(): Observable<IllustListResp>
}