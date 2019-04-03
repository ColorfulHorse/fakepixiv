package com.lyj.fakepivix.module.login.login

import android.arch.lifecycle.LifecycleOwner
import com.lyj.fakepivix.app.base.BaseViewModel

/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc 登录
 */
class LoginViewModel : BaseViewModel<ILoginModel>() {

    override var mModel: ILoginModel = LoginModel()

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
    }

}