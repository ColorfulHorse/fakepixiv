package com.lyj.fakepivix.module.main.home.illust

import android.arch.lifecycle.LifecycleOwner
import android.databinding.ObservableArrayList
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
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
    var loadState: LoadState = LoadState.Idle

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

    }

    fun lazyLoad() {
        liveViewModel.load()
        pixivisionViewModel.load()
        val disposable = IllustRepository.instance
                .loadRecommend()
                .doOnSubscribe { loadState = LoadState.Loading }
                .subscribeBy(onNext = {
                    loadState = LoadState.Succeed
                    data.clear()
                    data.addAll(it.illusts)
                    rankViewModel.onData(it.ranking_illusts)
                }, onError = {
                    loadState = LoadState.Failed(it)
                })
        addDisposable(disposable)
    }

}