package com.lyj.fakepivix.app.utils

import android.support.v4.app.FragmentActivity
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.module.main.illust.IllustDetailRootFragment
import me.yokeyword.fragmentation.SupportHelper

/**
 * @author greensun
 *
 * @date 2019/6/28
 *
 * @desc
 */
object Router {

    fun goDetail(position: Int, data: List<Illust>) {
        AppManager.instance.top?.let {
            val top = SupportHelper.getTopFragment((it as FragmentActivity).supportFragmentManager) as FragmentationFragment<*, *>
            top.start(IllustDetailRootFragment.newInstance(position, data))
        }
    }

    fun getActiveFragment(): FragmentationFragment<*, *>? {
        AppManager.instance.top?.let {
            return SupportHelper.getActiveFragment((it as FragmentActivity).supportFragmentManager) as FragmentationFragment<*, *>
        }
        return null
    }
}