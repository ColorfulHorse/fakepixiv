package com.lyj.fakepixiv.module.login.register

import androidx.lifecycle.LifecycleOwner
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import com.lyj.fakepixiv.BR
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.data.source.remote.UserRepository
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.app.utils.ToastUtil
import com.lyj.fakepixiv.app.utils.ToastUtil.toast
import kotlinx.coroutines.*
import timber.log.Timber

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
            val resp = withContext(Dispatchers.IO) {
                UserRepository.instance
                        .register(userName)
            }
            loadState.set(LoadState.Succeed)
        }
    }
}