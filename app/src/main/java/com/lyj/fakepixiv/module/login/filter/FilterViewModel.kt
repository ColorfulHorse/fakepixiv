package com.lyj.fakepixiv.module.login.filter

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.databinding.library.baseAdapters.BR
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.data.model.response.LoginData
import com.lyj.fakepixiv.app.data.source.remote.UserRepository
import com.lyj.fakepixiv.app.databinding.onPropertyChangedCallback
import com.lyj.fakepixiv.app.utils.ToastUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc 登录
 */
class FilterViewModel : BaseViewModel() {

    var loginEnable = ObservableField(false)

    var loading = ObservableField(false)

    @get:Bindable
    var userName = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.userName)
        }

    @get:Bindable
    var password = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.password)
        }


    init {
        addOnPropertyChangedCallback(onPropertyChangedCallback { _, id ->
            when (id) {
                BR.userName, BR.password -> {
                    if (userName.isEmpty() or password.isEmpty()) {
                        loginEnable.set(false)
                    } else {
                        loginEnable.set(true)
                    }
                }
            }
        })
    }

    fun login(code: String) {
        launch {
            flow<LoginData> {
                emit(UserRepository.instance.loginV2(code, UserRepository.instance.code_verifier))
            }
                    .flowOn(Dispatchers.IO)
                    .catch {

                    }
                    .collect {

                    }
        }
    }

}