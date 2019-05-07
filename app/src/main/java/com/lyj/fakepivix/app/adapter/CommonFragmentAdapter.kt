package com.lyj.fakepivix.app.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * @author greensun
 *
 * @date 2019/5/5
 *
 * @desc
 */
class CommonFragmentAdapter(fm: FragmentManager, private val fragments: List<Fragment>, private val titles: Array<out CharSequence>? = null) : FragmentPagerAdapter(fm) {

    override fun getItem(pos: Int): Fragment = fragments[pos]

    override fun getCount(): Int = fragments.size

    override fun getPageTitle(position: Int): CharSequence? {
        titles?.let {
            return titles[position]
        }
        return super.getPageTitle(position)
    }
}