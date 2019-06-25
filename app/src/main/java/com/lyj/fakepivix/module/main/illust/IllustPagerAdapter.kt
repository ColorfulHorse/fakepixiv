package com.lyj.fakepivix.module.main.illust

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import com.lyj.fakepivix.app.data.model.response.Illust
import timber.log.Timber

/**
 * @author greensun
 *
 * @date 2019/5/30
 *
 * @desc
 */
class IllustPagerAdapter(val data: List<Illust>, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    val fragments: SparseArray<IllustDetailFragment> = SparseArray()

    var feature: ((IllustDetailFragment) -> Unit)? = null
    var position: Int = 0

    override fun getItem(position: Int): Fragment {
        Log.e("xxx", "getItem: $position")
        val fragment = IllustDetailFragment.newInstance(position)
        fragments.put(position, fragment)
        feature?.let {
            if (position == this.position) {
                it.invoke(fragment)
            }
        }
        return fragment
    }

    override fun getCount(): Int = data.size

    fun getFragment(position: Int): IllustDetailFragment? {
        return fragments[position]
    }

    /**
     * 异步获取
     */
    fun getFragment(position: Int, feature: (IllustDetailFragment) -> Unit) {
        this.position = position
        this.feature = feature
    }

}