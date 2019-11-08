package com.lyj.fakepixiv.module.main.news.follow

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import com.lyj.fakepixiv.app.base.BaseViewModel

import com.lyj.fakepixiv.app.data.model.response.UserPreview
import com.lyj.fakepixiv.app.data.source.remote.UserRepository
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
class UserHeaderViewModel : BaseViewModel() {


    val data: ObservableList<UserPreview> = ObservableArrayList()

    var user1: ObservableField<UserPreview> = ObservableField()
    var user2: ObservableField<UserPreview> = ObservableField()
    var user3: ObservableField<UserPreview> = ObservableField()

    fun load() {
        launch(CoroutineExceptionHandler { _, err ->

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