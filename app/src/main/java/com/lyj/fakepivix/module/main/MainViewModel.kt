package com.lyj.fakepivix.module.main

import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.data.source.UserRepository

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