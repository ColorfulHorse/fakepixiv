package com.lyj.fakepivix.module.login

import android.arch.lifecycle.LifecycleOwner
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.module.login.register.IRegisterModel
import com.lyj.fakepivix.module.login.register.RegisterModel

/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc 登录页滚动背景
 */
class WallpaperViewModel : BaseViewModel<IRegisterModel>() {

    override var mModel: IRegisterModel = RegisterModel()

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

    }

}