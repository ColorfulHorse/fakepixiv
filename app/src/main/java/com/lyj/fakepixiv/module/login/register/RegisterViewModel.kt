package com.lyj.fakepixiv.module.login.register

import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.databinding.library.baseAdapters.BR
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.data.source.remote.UserRepository
import com.lyj.fakepixiv.app.network.ApiException
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.app.utils.AppManager
import com.lyj.fakepixiv.app.utils.Router
import com.lyj.fakepixiv.app.utils.ToastUtil
import com.lyj.fakepixiv.app.utils.startActivity
import com.lyj.fakepixiv.module.main.MainActivity
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
            ToastUtil.showToast(R.string.register_failed)
        }) {
            loadState.set(LoadState.Loading)
            val resp = withContext(Dispatchers.IO) {
                val resp = UserRepository.instance
                        .register(userName)
                if (resp.error) {
                    throw ApiException(ApiException.CODE_TOKEN_INVALID)
                }
                resp
            }
            val loginData = UserRepository.instance.login(resp.body.user_account, resp.body.password, resp.body.device_token, true)
            loadState.set(LoadState.Succeed)
            AppManager.instance.top?.startActivity(MainActivity::class.java)
        }
    }
}