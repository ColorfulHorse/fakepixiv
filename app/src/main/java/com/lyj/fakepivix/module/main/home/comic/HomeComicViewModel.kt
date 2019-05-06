package com.lyj.fakepivix.module.main.home.comic

import android.arch.lifecycle.LifecycleOwner
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.source.remote.HomeComicRepository
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.module.main.home.illust.*
import io.reactivex.rxkotlin.subscribeBy

/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc 登录
 */
class HomeComicViewModel : BaseViewModel<IModel?>() {

    override var mModel: IModel? = null

    val rankViewModel: RankViewModel = RankViewModel()
    val pixivisionViewModel: PixivisionViewModel = PixivisionViewModel()

    val data = ObservableArrayList<Illust>()
    var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)
    var loadMoreState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    fun lazyLoad() {
        load()
    }

    fun load() {
        val disposable = HomeComicRepository.instance
                .loadRecommend()
                .doOnSubscribe {
                    loadState.set(LoadState.Loading)
                    data.clear()
                }
                .subscribeBy(onNext = {
                    loadState.set(LoadState.Succeed)
                    data.clear()
                    data.addAll(it.illusts)
                    rankViewModel.onData(it.ranking_illusts)
                }, onError = {
                    loadState.set(LoadState.Failed(it))
                })
        addDisposable(disposable)
    }

    fun loadMore() {
        if (loadMoreState.get() !is LoadState.Loading) {
            val disposable = HomeComicRepository.instance
                    .loadMore()
                    .doOnSubscribe { loadMoreState.set(LoadState.Loading) }
                    .subscribeBy(onNext = {
                        loadMoreState.set(LoadState.Succeed)
                        data.addAll(it.illusts)
                    }, onError = {
                        loadMoreState.set(LoadState.Failed(it))
                    })
            addDisposable(disposable)
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        HomeComicRepository.instance.clear()
    }

}