package com.lyj.fakepixiv.module.login.register

import android.arch.lifecycle.LifecycleOwner
import android.databinding.Bindable
import android.databinding.ObservableField
import com.lyj.fakepixiv.BR
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.data.source.remote.UserRepository
import com.lyj.fakepixiv.app.network.LoadState
import kotlinx.coroutines.*

/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc 登录
 */
class RegisterViewModel : BaseViewModel() {

    val keyboardOpened: ObservableField<Boolean> = ObservableField(false)

    @get:Bindable
    var userName: String = ""
    set(value) {
        field = value
        notifyPropertyChanged(BR.userName)
    }

    val loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)


    fun load() {
        launch(CoroutineExceptionHandler { _, err ->
            loadState.set(LoadState.Failed(err))
        }) {
            loadState.set(LoadState.Loading)
            register()
            loadState.set(LoadState.Succeed)
        }
    }

    suspend fun register(): Any {
        val resp = withContext(Dispatchers.IO + CoroutineExceptionHandler { _, err ->
            loadState.set(LoadState.Failed(err))
        }) {
            UserRepository.instance
                    .register(userName)
        }
        return resp
    }
}