package com.lyj.fakepixiv.module.setting.account

import androidx.databinding.Bindable
import com.lyj.fakepixiv.BR
import com.lyj.fakepixiv.app.base.BaseViewModel

/**
 * @author green sun
 *
 * @date 2019/11/20
 *
 * @desc
 */
class AccountViewModel : BaseViewModel() {
    @get:Bindable
    var email: String = ""
    set(value) {
        field = value
        notifyPropertyChanged(BR.email)
    }
    @get:Bindable
    var id: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }
    @get:Bindable
    var password: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.password)
        }
}