package com.lyj.fakepixiv.module.main.home.illust

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.base.IModel
import com.lyj.fakepixiv.app.data.model.response.Live
import com.lyj.fakepixiv.app.data.source.remote.LiveRepository
import com.lyj.fakepixiv.app.network.LoadState
import io.reactivex.rxkotlin.subscribeBy

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc
 */
class LiveViewModel : BaseViewModel<IModel?>() {
    override val mModel: IModel? = null

    val data = ObservableArrayList<Live>()

    var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    fun load() {
        val disposable = LiveRepository.instance
                .loadRecommend()
                .doOnSubscribe{
                    loadState.set(LoadState.Loading)
                }
                .subscribeBy(onNext = {
                    loadState.set(LoadState.Succeed)
                    data.clear()
                    data.addAll(it.lives)
                }, onError = {
                    loadState.set(LoadState.Failed(it))
                })
        addDisposable(disposable)
    }

    fun onData(lives: List<Live>) {
        data.clear()
        data.addAll(lives)
    }

}