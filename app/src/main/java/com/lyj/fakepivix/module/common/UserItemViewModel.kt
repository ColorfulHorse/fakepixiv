package com.lyj.fakepivix.module.common

import android.databinding.ObservableField
import com.lyj.fakepivix.app.adapter.PreloadModel
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.data.model.response.UserPreview
import com.lyj.fakepivix.app.data.source.remote.UserRepository
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.app.utils.Router
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

    init {
        parent + this
    }

    /**
     * 关注/取消关注
     */
    fun follow() {
        addDisposable(UserRepository.instance.follow(data.user, followState))
    }

    fun goDetail() {
        Router.goUserDetail(data.user)
    }
}