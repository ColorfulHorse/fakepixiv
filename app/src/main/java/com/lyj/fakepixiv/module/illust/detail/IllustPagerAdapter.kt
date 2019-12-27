package com.lyj.fakepixiv.module.illust.detail

import androidx.fragment.app.Fragment
import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.lyj.fakepixiv.app.data.model.response.Illust

/**
 * @author greensun
 *
 * @date 2019/5/30
 *
 * @desc
 */
class IllustPagerAdapter(val data: List<Illust>, fm: FragmentManager, val key: Int) : FragmentStatePagerAdapter(fm) {
    val fragments: SparseArray<IllustDetailFragment> = SparseArray()

    var feature: ((IllustDetailFragment) -> Unit)? = null
    var position: Int = 0

    override fun getItem(position: Int): Fragment {
        val fragment = if (key == -1) {
            IllustDetailFragment.newInstance(illustId = data[position].id)
        }else {
            IllustDetailFragment.newInstance(position, key)
        }
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

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        fragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

}