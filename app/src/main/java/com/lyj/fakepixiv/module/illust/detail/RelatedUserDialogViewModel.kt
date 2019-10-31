package com.lyj.fakepixiv.module.illust.detail

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.util.SparseBooleanArray
import com.lyj.fakepixiv.app.base.BaseViewModel

import com.lyj.fakepixiv.app.data.model.response.User
import com.lyj.fakepixiv.app.data.model.response.UserPreview
import com.lyj.fakepixiv.app.data.source.remote.UserRepository
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.app.utils.Router
import io.reactivex.rxkotlin.subscribeBy

/**
 * @author greensun
 *
 * @date 2019/6/3
 *
 * @desc 相关用户
 */
class RelatedUserDialogViewModel(var user: User = User()) : BaseViewModel() {


    var data = ObservableArrayList<UserPreview>()

    var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    // 是否正在请求
    val followStateList = SparseBooleanArray()


    fun load() {
        val disposable = UserRepository.instance
                .getRelatedUsers(user.id)
                .doOnSubscribe { loadState.set(LoadState.Loading) }
                .subscribeBy(onNext = {
                    data.addAll(it.user_previews)
                    loadState.set(LoadState.Succeed)
                    showDialog()
                }, onError = {
                    loadState.set(LoadState.Failed(it))
                })
        addDisposable(disposable)
    }

    fun showDialog() {
        // 数据加载完成弹出dialog
        Router.getTopFragmentManager()?.let {
            val dialogFragment = RelatedUserDialogFragment.newInstance()
            dialogFragment.mViewModel = this
            dialogFragment.show(it, "RelatedUserDialogFragment")
        }
    }

    fun follow(position: Int) {
        if (!followStateList[position]) {
            val user = data[position].user
            var followed = user.is_followed
            val disposable = UserRepository.instance
                    .follow(user.id, !followed)
                    .doOnSubscribe { followStateList.put(position, true) }
                    .subscribeBy(onNext = {
                        user.is_followed = !followed
                        followStateList.put(position, false)
                    }, onError = {
                        followStateList.put(position, false)
                    })
            addDisposable(disposable)
        }
    }
}