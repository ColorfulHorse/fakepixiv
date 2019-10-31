package com.lyj.fakepixiv.module.main.home.comic

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.lyj.fakepixiv.app.base.BaseViewModel

import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.constant.IllustCategory.*
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.data.source.remote.IllustRepository
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.module.main.home.illust.*
import io.reactivex.rxkotlin.subscribeBy

/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc 登录
 */
class HomeComicViewModel : BaseViewModel() {

    val rankViewModel: RankViewModel = RankViewModel()
    val pixivisionViewModel: PixivisionViewModel = PixivisionViewModel().apply {
        category = IllustCategory.COMIC
    }

    val data = ObservableArrayList<Illust>()
    var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)
    var loadMoreState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var nextUrl = ""

    init {
        this + rankViewModel + pixivisionViewModel
    }

    fun lazyLoad() {
        load()
    }

    fun load() {
        val disposable = IllustRepository.instance
                .loadRecommendIllust(COMIC)
                .doOnSubscribe {
                    loadState.set(LoadState.Loading)
                    //pixivisionViewModel.load()
                }
                .subscribeBy(onNext = {
                    loadState.set(LoadState.Succeed)
                    nextUrl = it.next_url
                    data.clear()
                    data.addAll(it.illusts)
                    rankViewModel.onData(it.ranking_illusts)
                }, onError = {
                    loadState.set(LoadState.Failed(it))
                })
        addDisposable(disposable)
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
}