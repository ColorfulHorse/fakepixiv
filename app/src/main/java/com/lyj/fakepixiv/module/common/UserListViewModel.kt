package com.lyj.fakepixiv.module.common

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import com.lyj.fakepixiv.app.base.BaseViewModel

import com.lyj.fakepixiv.app.data.model.response.UserPreviewListResp
import com.lyj.fakepixiv.app.data.source.remote.UserRepository
import com.lyj.fakepixiv.app.network.ApiException
import com.lyj.fakepixiv.app.network.LoadState
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
class UserListViewModel(var action: (suspend () -> UserPreviewListResp)) : BaseViewModel() {


    val data: ObservableList<UserItemViewModel> = ObservableArrayList()

    var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var loadMoreState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var nextUrl = ""

    fun load() {
        launch(CoroutineExceptionHandler { _, err ->
            loadState.set(LoadState.Failed(err))
        }) {
            loadState.set(LoadState.Loading)
            val resp = withContext(Dispatchers.IO) {
                action.invoke()
            }
            if (resp.user_previews.isEmpty()) {
                throw ApiException(ApiException.CODE_EMPTY_DATA)
            }
            data.clear()
            data.addAll(resp.user_previews.map { UserItemViewModel(this@UserListViewModel, it) })
            nextUrl = resp.next_url
            loadState.set(LoadState.Succeed)
        }
    }

    fun loadMore() {
        if (nextUrl.isBlank())
            return
        launch(CoroutineExceptionHandler { _, err ->
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