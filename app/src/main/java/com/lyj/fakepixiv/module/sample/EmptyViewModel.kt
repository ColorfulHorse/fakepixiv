package com.lyj.fakepixiv.module.sample

import android.arch.lifecycle.LifecycleOwner
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.module.login.register.IRegisterModel
import com.lyj.fakepixiv.module.login.register.RegisterModel

/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc 登录
 */
class EmptyViewModel : BaseViewModel<IRegisterModel>() {

    override var mModel: IRegisterModel = RegisterModel()

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
    }

}