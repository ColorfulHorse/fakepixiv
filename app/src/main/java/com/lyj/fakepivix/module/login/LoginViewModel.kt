package com.lyj.fakepivix.module.login

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.Observable
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.app.utils.LogUtils

/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc
 */
class LoginViewModel : BaseObservable() {
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

    init {
        addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                when(propertyId) {
                    BR.userName -> LogUtils.debugInfo("userName Changed")
                }
            }
        })
    }
}