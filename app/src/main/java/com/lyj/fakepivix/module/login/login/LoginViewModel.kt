package com.lyj.fakepivix.module.login.login

import android.databinding.Bindable
import android.databinding.ObservableField
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.data.source.remote.UserRepository
import com.lyj.fakepivix.app.databinding.onPropertyChangedCallback
import com.lyj.fakepivix.app.network.LoadState
import io.reactivex.rxkotlin.subscribeBy

/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc 登录
 */
class LoginViewModel : BaseViewModel<ILoginModel>() {

    var keyboardOpened = ObservableField(false)

    // 是否加载完成
    @set:Synchronized
    var loginState : ObservableField<LoadState> = ObservableField(LoadState.Idle)

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

    override var mModel: ILoginModel = LoginModel()

    init {
        addOnPropertyChangedCallback(onPropertyChangedCallback {
            _, id ->
            when(id) {
                BR.userName, BR.password -> {
                    if (userName.isEmpty() or password.isEmpty()) {
                        loginEnable.set(false)
                    }else {
                        loginEnable.set(true)
                    }
                }
            }
        })
    }


    fun login() {
        val disposable = UserRepository.instance
                .login(userName, password)
                .doOnSubscribe {
                    loginState.set(LoadState.Loading)
                    loading.set(true)
                }
                .subscribeBy(onNext = {
                    loginState.set(LoadState.Succeed)
                    loading.set(false)
                }, onError = {
                    loginState.set(LoadState.Failed(it))
                    loading.set(false)
                })
        addDisposable(disposable)
    }

}