package com.lyj.fakepivix.module.common

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.data.model.response.UserPreviewListResp
import com.lyj.fakepivix.app.data.source.remote.UserRepository
import com.lyj.fakepivix.app.network.LoadState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author greensun
 *
 * @date 2019/8/6
 *
 * @desc 用户预览列表
 */
class UserListViewModel(var action: (suspend () -> UserPreviewListResp)) : BaseViewModel<IModel?>() {
    override val mModel: IModel? = null

    val data: ObservableList<UserItemViewModel> = ObservableArrayList()

    var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var loadMoreState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var nextUrl = ""

    fun load() {
        launch(Dispatchers.Main + CoroutineExceptionHandler { _, err ->
            loadState.set(LoadState.Failed(err))
        }) {
            loadState.set(LoadState.Loading)
            data.clear()
            val resp = withContext(Dispatchers.IO) {
                action.invoke()
            }
            data.addAll(resp.user_previews.map { UserItemViewModel(this@UserListViewModel, it) })
            nextUrl = resp.next_url
            loadState.set(LoadState.Succeed)
        }
    }

    fun loadMore() {
        if (nextUrl.isBlank())
            return
        launch(Dispatchers.Main + CoroutineExceptionHandler { _, err ->
            loadMoreState.set(LoadState.Failed(err))
        }) {
            loadMoreState.set(LoadState.Loading)
            val resp = withContext(Dispatchers.IO) {
                UserRepository.instance
                        .loadMore(nextUrl)
            }
            data.addAll(resp.user_previews.map { UserItemViewModel(this@UserListViewModel, it) })
            nextUrl = resp.next_url
            loadMoreState.set(LoadState.Succeed)
        }
    }

}