package com.lyj.fakepivix.module.main.illust

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.ViewGroup
import com.lyj.fakepivix.app.data.model.response.Illust

/**
 * @author greensun
 *
 * @date 2019/5/30
 *
 * @desc
 */
class IllustPagerAdapter(val data: List<Illust>, val currentPosition: Int, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    var lastPosition = -1

    override fun getItem(position: Int): Fragment {
        var pos = currentPosition
        if (lastPosition == -1) {

        }
        position - lastPosition
        lastPosition
        return IllustDetailFragment.newInstance(position)
    }


    override fun getCount(): Int = data.size

}