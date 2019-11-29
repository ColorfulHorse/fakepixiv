package com.lyj.fakepixiv.app.utils

import androidx.fragment.app.*
import com.bumptech.glide.manager.SupportRequestManagerFragment
import com.lyj.fakepixiv.app.base.FragmentationFragment
import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.data.model.response.User
import com.lyj.fakepixiv.app.data.source.remote.IllustRepository
import com.lyj.fakepixiv.app.data.source.remote.UserRepository
import com.lyj.fakepixiv.module.illust.detail.IllustDetailRootFragment
import com.lyj.fakepixiv.module.illust.ranking.RankIllustRootFragment
import com.lyj.fakepixiv.module.illust.series.ComicSeriesFragment
import com.lyj.fakepixiv.module.novel.series.NovelSeriesFragment
import com.lyj.fakepixiv.module.main.search.main.SearchMainFragment
import com.lyj.fakepixiv.module.novel.NovelDetailFragment
import com.lyj.fakepixiv.module.novel.NovelDialogFragment
import com.lyj.fakepixiv.module.user.detail.UserDetailFragment
import me.yokeyword.fragmentation.SupportHelper

/**
 * @author greensun
 *
 * @date 2019/6/28
 *
 * @desc 路由
 */
object Router {

    /**
     *  跳转到详情页
     */
    fun goDetail(position: Int, data: List<Illust>) {
        closeDialog()
        val fm = getTopFragmentManager()
        fm?.let {
            if (data.isNotEmpty()) {
                val top = SupportHelper.getTopFragment(fm) as FragmentationFragment<*, *>
                val key = (System.currentTimeMillis()/1000).toInt()
                val illust = data[position]
                IllustRepository.instance[key] = data.filter { it.id != 0L }
                if (illust.itemType and Illust.TYPE_NOVEL == Illust.TYPE_NOVEL) {
                    NovelDialogFragment.newInstance(position, key).show(fm, "NovelDialogFragment-$key")
                }else {
                    val fragment = IllustDetailRootFragment.newInstance(position, key)
                    top.start(fragment)
                }
            }
        }
    }

    /**
     * 不带数据跳转到详情页
     */
    fun goDetail(illustId: Long) {
        closeDialog()
        getTopFragment()?.let {
            val fragment = IllustDetailRootFragment.newInstance(illustId = illustId)
            it.start(fragment)
        }
    }

    /**
     * 小说详情页
     */
    fun goNovelDetail(key: Int, position: Int) {
        val fragment = NovelDetailFragment.newInstance(position, key)
        getTopFragment()?.start(fragment)
    }

    /**
     * 列表直接跳转到小说详情页
     */
    fun goNovelDetail(position: Int, data: List<Illust>) {
        if (data.isNotEmpty()) {
            val key = (System.currentTimeMillis()/1000).toInt()
            IllustRepository.instance[key] = data.filter { it.id != 0L }
            val fragment = NovelDetailFragment.newInstance(position, key)
            getTopFragment()?.start(fragment)
        }
    }

    /**
     * 排行榜
     */
    fun goRank(@IllustCategory category: String) {
        val fragment = RankIllustRootFragment.newInstance(category)
        getTopFragment()?.start(fragment)
    }

    /**
     * 用户详情页
     */
    fun goUserDetail(user: User? = null, userId: Long = -1) {
        var id = userId
        if (user != null) {
            UserRepository.instance[user.id] = user
            id = user.id
        }
        val fragment = UserDetailFragment.newInstance(id)
        getTopFragment()?.start(fragment)
    }

    /**
     * 搜索页
     */
    fun goSearch(@IllustCategory category: String, keyword: String = "") {
        closeDialog()
        getTopFragment()?.start(SearchMainFragment.newInstance(category, keyword))
    }

    fun goIllustSeries(seriesId: String) {
        closeDialog()
        getTopFragment()?.start(ComicSeriesFragment.newInstance(seriesId))
    }

    fun goNovelSeries(seriesId: String) {
        closeDialog()
        getTopFragment()?.start(NovelSeriesFragment.newInstance(seriesId))
    }

    private fun closeDialog() {
        getRealActiveFragment()?.let {
            if (it is DialogFragment) {
                it.dismiss()
            }else {
                it.childFragmentManager.fragments.forEach {
                    if (it is DialogFragment) {
                        it.dismiss()
                    }
                }
            }
        }
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

    /**
     * 获取顶部活动的 support fragment
     */
    fun getActiveFragment(): FragmentationFragment<*, *>? {
        AppManager.instance.top?.let {
            val f = SupportHelper.getActiveFragment((it as FragmentActivity).supportFragmentManager)
            if (f is FragmentationFragment<*, *>) {
                return f
            }
        }
        return null
    }

    /**
     * 获取顶部活动的fragment
     */
    fun getRealActiveFragment(): Fragment? {
        AppManager.instance.top?.let {
            if (it is FragmentActivity) {
                return getRealActiveFragment(it.supportFragmentManager)
            }
        }
        return null
    }

    fun getRealActiveFragment(fm: FragmentManager, parent: Fragment? = null): Fragment? {
        val fragmentList = FragmentationMagician.getActiveFragments(fm)
        if (fragmentList.isEmpty())
            return parent
        for (i in fragmentList.indices.reversed()) {
            val fragment = fragmentList[i] ?: continue
            if (fragment is SupportRequestManagerFragment) {
                continue
            }
            if (fragment.isResumed && !fragment.isHidden && fragment.userVisibleHint) {
                return getRealActiveFragment(fragment.childFragmentManager, fragment)
            }
        }
        return parent
    }

}