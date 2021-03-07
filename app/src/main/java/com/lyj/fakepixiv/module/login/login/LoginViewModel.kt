package com.lyj.fakepixiv.module.login.login

import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.databinding.library.baseAdapters.BR
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.data.source.remote.UserRepository
import com.lyj.fakepixiv.app.databinding.onPropertyChangedCallback
import com.lyj.fakepixiv.app.network.LoadState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc 登录
 */
class LoginViewModel : BaseViewModel() {

    var keyboardOpened = ObservableField(false)

    // 是否加载完成
    @set:Synchronized
    var loginState : ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var loginEnable = ObservableField(false)

    var loading = ObservableField(false)

//    @get:Bindable
//    var userName = ""
//        set(value) {
//            field = value
//            notifyPropertyChanged(BR.userName)
//        }
//
//    @get:Bindable
//    var password = ""
//        set(value) {
//            field = value
//            notifyPropertyChanged(BR.password)
//        }


    init {
//        addOnPropertyChangedCallback(onPropertyChangedCallback {
//            _, id ->
//            when(id) {
//                BR.userName, BR.password -> {
//                    if (userName.isEmpty() or password.isEmpty()) {
//                        loginEnable.set(false)
//                    }else {
//                        loginEnable.set(true)
//                    }
//                }
//            }
//        })
    }


//    fun login() {
//        val disposable = UserRepository.instance
//                .login(userName, password)
//                .doOnSubscribe {
//                    loginState.set(LoadState.Loading)
//                    loading.set(true)
//                }
//                .subscribeBy(onNext = {
//                    loginState.set(LoadState.Succeed)
//                    loading.set(false)
//                }, onError = {
//                    loginState.set(LoadState.Failed(it))
//                    loading.set(false)
//                })
//        addDisposable(disposable)
//    }

    fun login() {
//        launch(CoroutineExceptionHandler { _, err ->
//            loginState.set(LoadState.Failed(err))
//            loading.set(false)
//        }) {
//            loginState.set(LoadState.Loading)
//            loading.set(true)
//            UserRepository.instance.login(userName, password)
//            loginState.set(LoadState.Succeed)
//            loading.set(false)
//        }
    }

}