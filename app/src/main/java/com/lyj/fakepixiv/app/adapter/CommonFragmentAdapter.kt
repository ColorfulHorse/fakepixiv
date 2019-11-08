package com.lyj.fakepixiv.app.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

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