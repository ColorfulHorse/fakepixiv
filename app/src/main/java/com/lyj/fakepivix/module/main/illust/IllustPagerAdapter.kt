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
class IllustPagerAdapter(val data: List<Illust>, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return IllustDetailFragment.newInstance(position)
    }


    override fun getCount(): Int = data.size

}