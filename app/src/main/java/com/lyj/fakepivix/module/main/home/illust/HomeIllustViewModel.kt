package com.lyj.fakepivix.module.main.home.illust

import android.arch.lifecycle.LifecycleOwner
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.source.remote.HomeComicRepository
import com.lyj.fakepivix.app.data.source.remote.HomeIllustRepository
import com.lyj.fakepivix.app.network.LoadState
import io.reactivex.rxkotlin.subscribeBy

/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc 登录
 */
class HomeIllustViewModel : BaseViewModel<IHomeIllustModel>() {

    override var mModel: IHomeIllustModel = HomeIllustModel()

    val rankViewModel: RankViewModel = RankViewModel()
    val liveViewModel: LiveViewModel = LiveViewModel()
    val pixivisionViewModel: PixivisionViewModel = PixivisionViewModel()

    val data = ObservableArrayList<Illust>()
    var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)
    var loadMoreState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

    }

    fun lazyLoad() {
//        val liveOb = LiveRepository.instance
//                .loadRecommend()
//        val illustOb = IllustRepository.instance
//                .loadRecommend()
//        val disposable = Observables.combineLatest(liveOb, illustOb) {
//            lives, res ->
//            RecommendGroup(lives, res)
//        }
//                .doOnSubscribe { loadState = LoadState.Loading }
//                .subscribeBy(onNext = {
//                    loadState = LoadState.Succeed
//                    data.clear()
//                    data.addAll(it.illustListResp.illusts)
//                    rankViewModel.onData(it.illustListResp.ranking_illusts)
//                    liveViewModel.onData(it.lives)
//                }, onError = {
//                    loadState = LoadState.Failed(it)
//                })

        liveViewModel.load()
        load()
    }

    fun load() {
        when (liveViewModel.loadState.get()) {
            is LoadState.Idle, is LoadState.Failed -> liveViewModel.load()
        }
        val disposable = HomeIllustRepository.instance
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
            val disposable = HomeIllustRepository.instance
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
        HomeIllustRepository.instance.clear()
    }
}