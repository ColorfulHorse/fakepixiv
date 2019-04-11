package com.lyj.fakepivix.module.login.login

import android.arch.lifecycle.LifecycleOwner
import android.databinding.Bindable
import android.databinding.ObservableField
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.data.source.UserRepository
import com.lyj.fakepivix.app.databinding.OnPropertyChangedCallbackImp
import com.lyj.fakepivix.app.reactivex.schedulerTransformer

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
    var loading = ObservableField(false)

    var loginEnable = ObservableField(false)

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
        addOnPropertyChangedCallback(OnPropertyChangedCallbackImp {
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

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
    }

    fun login() {
        UserRepository.instance
                .login(userName, password)
                .schedulerTransformer()
                .

    }

}