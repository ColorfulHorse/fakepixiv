package com.lyj.fakepixiv.module.common

import android.databinding.ObservableField
import com.lyj.fakepixiv.app.adapter.PreloadModel
import com.lyj.fakepixiv.app.base.BaseViewModel

import com.lyj.fakepixiv.app.data.model.response.UserPreview
import com.lyj.fakepixiv.app.data.source.remote.UserRepository
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.app.utils.Router

/**
 * @author greensun
 *
 * @date 2019/8/10
 *
 * @desc
 */
class UserItemViewModel(val parent: BaseViewModel, val data: UserPreview) : BaseViewModel(), PreloadModel by data {


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