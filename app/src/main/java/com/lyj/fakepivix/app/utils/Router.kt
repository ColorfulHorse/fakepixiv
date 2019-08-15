package com.lyj.fakepivix.app.utils

import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.module.main.illust.IllustDetailRootFragment
import com.lyj.fakepivix.module.main.novel.NovelDetailFragment
import com.lyj.fakepivix.module.main.novel.NovelDialogFragment
import me.yokeyword.fragmentation.SupportHelper

/**
 * @author greensun
 *
 * @date 2019/6/28
 *
 * @desc 路由
 */
object Router {

    fun goDetail(position: Int, data: List<Illust>) {
        val fm = getTopFragmentManager()
        fm?.let {
            val top = SupportHelper.getTopFragment(fm) as FragmentationFragment<*, *>
            val key = (System.currentTimeMillis()/1000).toInt()
            val illust = data[position]
            IllustRepository.instance[key] = data
            if (illust.itemType == Illust.TYPE_NOVEL) {
                NovelDialogFragment.newInstance(position, key).show(fm, "NovelDialogFragment-$key")
            }else {
                val fragment = IllustDetailRootFragment.newInstance(position, key)
                top.start(fragment)
            }
        }
    }

    fun goNovelDetail(key: Int, position: Int) {
        val fragment = NovelDetailFragment.newInstance(position, key)
        getTopFragment()?.start(fragment)
    }

    fun getTopFragmentManager(): FragmentManager? {
        return AppManager.instance.top?.let {
            (it as FragmentActivity).supportFragmentManager
        }
    }

    fun getTopFragment(): FragmentationFragment<*, *>? {
        val fm = getTopFragmentManager()
        return fm?.let {
            SupportHelper.getTopFragment(fm) as FragmentationFragment<*, *>
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