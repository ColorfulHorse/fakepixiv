package com.lyj.fakepixiv.module.main

import androidx.lifecycle.LifecycleOwner
import com.lyj.fakepixiv.app.base.BaseViewModel

import com.lyj.fakepixiv.app.data.source.remote.UserRepository
import com.lyj.fakepixiv.app.utils.EmojiUtil

/**
 * @author greensun
 *
 * @date 2019/4/12
 *
 * @desc
 */
class MainViewModel : BaseViewModel() {

    val user = UserRepository.instance.loginData?.user

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        EmojiUtil.init()
    }
}