package com.lyj.fakepivix.module.main.illust

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.util.SparseBooleanArray
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.data.model.response.UserPreview
import com.lyj.fakepivix.app.data.source.remote.UserRepository
import com.lyj.fakepivix.app.network.LoadState
import io.reactivex.rxkotlin.subscribeBy

/**
 * @author greensun
 *
 * @date 2019/6/3
 *
 * @desc 详情页用户item
 */
class RelatedUserDialogViewModel(val parent: IllustDetailViewModel) : BaseViewModel<IModel?>() {
    override val mModel: IModel? = null

    var data = ObservableArrayList<UserPreview>()

    var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    // 是否正在请求
    val followStateList = SparseBooleanArray()


    fun load() {
        val disposable = UserRepository.instance
                .getRelatedUsers(parent.illust.user?.id.toString())
                .doOnSubscribe { loadState.set(LoadState.Loading) }
                .subscribeBy(onNext = {
                    data.addAll(it.user_previews)
                    loadState.set(LoadState.Succeed)
                }, onError = {
                    loadState.set(LoadState.Failed(it))
                })
        addDisposable(disposable)
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