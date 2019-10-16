package com.lyj.fakepixiv.module.main

import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.base.IModel
import com.lyj.fakepixiv.app.data.source.remote.UserRepository

/**
 * @author greensun
 *
 * @date 2019/4/12
 *
 * @desc
 */
class MainViewModel : BaseViewModel<IModel?>() {

    override val mModel: IModel? = null

    val user = UserRepository.instance.loginData?.user
}