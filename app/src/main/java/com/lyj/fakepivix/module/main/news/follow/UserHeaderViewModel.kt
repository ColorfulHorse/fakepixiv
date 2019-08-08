package com.lyj.fakepivix.module.main.news.follow

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.data.model.response.User
import com.lyj.fakepivix.app.data.model.response.UserPreview
import com.lyj.fakepivix.app.data.source.remote.UserRepository
import com.lyj.fakepivix.app.network.LoadState
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author greensun
 *
 * @date 2019/7/1
 *
 * @desc
 */
class UserHeaderViewModel : BaseViewModel<IModel?>() {
    override val mModel: IModel? = null

    val data: ObservableList<UserPreview> = ObservableArrayList()

    var user1: ObservableField<UserPreview> = ObservableField()
    var user2: ObservableField<UserPreview> = ObservableField()
    var user3: ObservableField<UserPreview> = ObservableField()

    fun load() {
        launch(Dispatchers.Main + CoroutineExceptionHandler { _, err ->

        }) {
            val res = withContext(Dispatchers.IO) {
                UserRepository.instance
                        .getRecommendUsers()
            }
            data.addAll(res.user_previews)
            user1.set(data.getOrNull(0))
            user2.set(data.getOrNull(1))
            user3.set(data.getOrNull(2))
        }
    }
}