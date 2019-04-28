package com.lyj.fakepivix.module.main.home

import android.os.Bundle
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.FragmentationFragment;
import com.lyj.fakepivix.app.entity.TabBean
import com.lyj.fakepivix.databinding.FragmentHomeBinding
import com.lyj.fakepivix.module.main.home.comic.HomeComicFragment
import com.lyj.fakepivix.module.main.home.illust.HomeIllustFragment
import com.lyj.fakepivix.module.main.home.novel.HomeNovelFragment


/**
 * @author greensun
 *
 * @date 2019/4/3
 *
 * @desc 主页fragment
 */
class HomeFragment : FragmentationFragment<FragmentHomeBinding, BaseViewModel<*>?>() {

    override var mViewModel: BaseViewModel<*>? = null

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val fragments = mutableListOf<FragmentationFragment<*, *>>()
    private var prePosition = 0


    override fun init(savedInstanceState: Bundle?) {
        initFragment()
        val tabs = arrayListOf<CustomTabEntity>(
                TabBean(title = getString(R.string.subtab_illust)),
                TabBean(title = getString(R.string.subtab_comic)),
                TabBean(title = getString(R.string.subtab_novel))
                )
        with(mBinding) {
            tabLayout.setTabData(tabs)
            tabLayout.currentTab = 0
            tabLayout.setOnTabSelectListener(object : OnTabSelectListener {
                override fun onTabSelect(position: Int) {
                    switchTab(position)
                }

                override fun onTabReselect(position: Int) {

                }
            })
        }
    }

    private fun initFragment() {
        var illustFragment = findFragment(HomeIllustFragment::class.java)
        var comicFragment = findFragment(HomeComicFragment::class.java)
        var novalFragment = findFragment(HomeNovelFragment::class.java)
        if (illustFragment == null) {
            illustFragment = HomeIllustFragment.newInstance()
        }
        if (comicFragment == null) {
            comicFragment = HomeComicFragment.newInstance()
        }
        if (novalFragment == null) {
            novalFragment = HomeNovelFragment.newInstance()
        }
        fragments.clear()
        fragments.add(illustFragment)
        fragments.add(comicFragment)
        fragments.add(novalFragment)
        loadMultipleRootFragment(R.id.fl_container, 0, fragments[0], fragments[1], fragments[2])
    }

    /**
     * 切换tab页
     */
    private fun switchTab(position: Int) {
        showHideFragment(fragments[position], fragments[prePosition])
        prePosition = position
    }

    override fun immersionBarEnabled(): Boolean = false

    override fun bindLayout(): Int = R.layout.fragment_home

}