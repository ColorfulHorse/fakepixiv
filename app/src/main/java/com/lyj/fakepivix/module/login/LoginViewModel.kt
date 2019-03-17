package com.lyj.fakepivix.module.login

import android.arch.lifecycle.LifecycleOwner
import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.Observable
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.app.base.BaseModel
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.utils.LogUtils

/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc
 */
class LoginViewModel : BaseViewModel<ILoginModel>() {

    override var mModel: ILoginModel = LoginModel()

    @get:Bindable
    var userName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.userName)
        }
    @get:Bindable
    var password: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.password)
        }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        userName = "lyj"
        password = "12132132"
    }

    fun login() {

    }
}