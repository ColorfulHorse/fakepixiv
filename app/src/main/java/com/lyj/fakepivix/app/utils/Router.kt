package com.lyj.fakepivix.app.utils

import android.arch.lifecycle.HolderFragment
import android.support.v4.app.FragmentActivity
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
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
            val key = (System.currentTimeMillis()/1000).toInt()
            val fragment = IllustDetailRootFragment.newInstance(position, key)
            IllustRepository.instance[key] = data
            top.start(fragment)
        }
    }

    fun getActiveFragment(): FragmentationFragment<*, *>? {
        AppManager.instance.top?.let {
            val f = SupportHelper.getActiveFragment((it as FragmentActivity).supportFragmentManager)
            if (f is FragmentationFragment<*, *>) {
                return f
            }
        }
        return null
    }
}