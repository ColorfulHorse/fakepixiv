package com.lyj.fakepixiv.app.entity

import com.flyco.tablayout.listener.CustomTabEntity

/**
 * @author greensun
 *
 * @date 2019/4/4
 *
 * @desc
 */
class TabBean(val inactiveIcon:Int = 0,  val activeIcon:Int = 0, val title: String = "") : CustomTabEntity {

    override fun getTabUnselectedIcon(): Int = inactiveIcon

    override fun getTabSelectedIcon(): Int = activeIcon

    override fun getTabTitle(): String = title
}