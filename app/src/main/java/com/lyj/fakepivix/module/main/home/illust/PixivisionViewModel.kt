package com.lyj.fakepivix.module.main.home.illust

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.data.model.response.SpotlightArticle
import com.lyj.fakepivix.app.data.source.remote.PixivisionRepository
import com.lyj.fakepivix.app.network.LoadState
import io.reactivex.rxkotlin.subscribeBy

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc
 */
class PixivisionViewModel : BaseViewModel<IModel?>() {
    override val mModel: IModel? = null

    val data = ObservableArrayList<SpotlightArticle>()

    var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    fun load() {
        val disposable = PixivisionRepository.instance
                .loadRecommend()
                .doOnSubscribe{ loadState.set(LoadState.Loading) }
                .subscribeBy(onNext = {
                    loadState.set(LoadState.Succeed)
                    data.clear()
                    data.addAll(it)
                }, onError = {
                    loadState.set(LoadState.Failed(it))
                })
        addDisposable(disposable)
    }

    fun refresh() {
        data.clear()
    }
}