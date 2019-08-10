package com.lyj.fakepivix.module.common

import android.databinding.ObservableField
import com.lyj.fakepivix.app.adapter.PreloadModel
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.data.model.response.UserPreview
import com.lyj.fakepivix.app.data.source.remote.UserRepository
import com.lyj.fakepivix.app.network.LoadState
import io.reactivex.rxkotlin.subscribeBy

/**
 * @author greensun
 *
 * @date 2019/8/10
 *
 * @desc
 */
class UserItemViewModel(val parent: BaseViewModel<*>, val data: UserPreview) : BaseViewModel<IModel?>(), PreloadModel by data {
    override val mModel: IModel? = null

    var followState: ObservableField<LoadState> = ObservableField(LoadState.Idle)


    /**
     * 关注/取消关注
     */
    fun follow() {
        with(data) {
            val followed = user.is_followed
            val disposable = UserRepository.instance
                    .follow(user.id, !followed)
                    .doOnSubscribe { followState.set(LoadState.Loading) }
                    .subscribeBy(onNext = {
                        user.is_followed = !followed
                        followState.set(LoadState.Succeed)
                    }, onError = {
                        followState.set(LoadState.Failed(it))
                    })
            parent.addDisposable(disposable)
        }
    }
}