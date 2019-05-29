package com.lyj.fakepivix.module.main.search.illust

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.constant.IllustCategory.ILLUST
import com.lyj.fakepivix.app.constant.Restrict

import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.model.response.IllustListResp
import com.lyj.fakepivix.app.data.model.response.TrendTag
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.data.source.remote.SearchRepository
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
class SearchIllustViewModel(@IllustCategory var category: String = ILLUST) : BaseViewModel<IModel?>() {

    override var mModel: IModel? = null

    val data = ObservableArrayList<TrendTag>()
    var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)
    var loadMoreState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var nextUrl = ""

    fun load() {
        val disposable = SearchRepository.instance
                .loadIllustSearchTag(category)
                .doOnSubscribe {
                    loadState.set(LoadState.Loading)
                }
                .subscribeBy(onNext = {
                    loadState.set(LoadState.Succeed)
                    data.clear()
                    data.addAll(it.trend_tags)
                }, onError = {
                    loadState.set(LoadState.Failed(it))
                })
        addDisposable(disposable)
    }

    fun loadMore() {
//        if (nextUrl.isBlank())
//        return
//        val disposable = IllustRepository.instance
//                .loadMore(nextUrl, category)
//                .doOnSubscribe { loadMoreState.set(LoadState.Loading) }
//                .subscribeBy(onNext = {
//                    loadMoreState.set(LoadState.Succeed)
//                    nextUrl = it.next_url
//                    data.addAll(it)
//                }, onError = {
//                    loadMoreState.set(LoadState.Failed(it))
//                })
//        addDisposable(disposable)
    }
}