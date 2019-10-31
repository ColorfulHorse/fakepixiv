package com.lyj.fakepixiv.module.main.news.follow

import com.lyj.fakepixiv.app.base.BaseViewModel


/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc
 */
class NewsFollowViewModel : BaseViewModel() {



    val userViewModel = UserHeaderViewModel()

    init {
        this + userViewModel
    }
}