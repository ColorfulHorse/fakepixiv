package com.lyj.fakepixiv.module.main.home.illust

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.constant.IllustCategory.ILLUST

import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.data.source.remote.IllustRepository
import com.lyj.fakepixiv.app.network.LoadState
import io.reactivex.rxkotlin.subscribeBy

/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc
 */
class HomeIllustViewModel : BaseViewModel() {

    val rankViewModel: RankViewModel = RankViewModel()
    val liveViewModel: LiveViewModel = LiveViewModel()
    val pixivisionViewModel: PixivisionViewModel = PixivisionViewModel().apply {
        category = IllustCategory.ILLUST
    }

    val data = ObservableArrayList<Illust>()
    var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)
    var loadMoreState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var nextUrl: String = ""


    fun load() {
        this + liveViewModel + pixivisionViewModel
        when (liveViewModel.loadState.get()) {
            is LoadState.Idle, is LoadState.Failed -> liveViewModel.load()
        }
        val disposable = IllustRepository.instance
                .loadRecommendIllust(ILLUST)
                .doOnSubscribe {
                    loadState.set(LoadState.Loading)
                    liveViewModel.load()
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
                .doOnSubscribe {
                    loadMoreState.set(LoadState.Loading) }
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