package com.lyj.fakepixiv.module.main.search.illust

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.lyj.fakepixiv.app.base.BaseViewModel

import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.constant.IllustCategory.ILLUST

import com.lyj.fakepixiv.app.data.model.response.TrendTag
import com.lyj.fakepixiv.app.data.source.remote.SearchRepository
import com.lyj.fakepixiv.app.network.LoadState
import io.reactivex.rxkotlin.subscribeBy

/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc
 */
class SearchTagViewModel(@IllustCategory var category: String = ILLUST) : BaseViewModel() {



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