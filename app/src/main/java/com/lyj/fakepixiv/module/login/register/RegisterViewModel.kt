package com.lyj.fakepixiv.module.login.register

import android.arch.lifecycle.LifecycleOwner
import android.databinding.ObservableField
import com.lyj.fakepixiv.app.base.BaseViewModel

/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc 登录
 */
class RegisterViewModel : BaseViewModel() {

    val keyboardOpened: ObservableField<Boolean> = ObservableField(false)

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

    }

}