package com.lyj.fakepivix.module.main.news.follow

import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel

/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc
 */
class NewsFollowViewModel : BaseViewModel<IModel?>() {

    override var mModel: IModel? = null

    val userViewModel = UserHeaderViewModel()
}