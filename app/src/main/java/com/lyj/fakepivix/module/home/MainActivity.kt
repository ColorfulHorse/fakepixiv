package com.lyj.fakepivix.module.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBarDrawerToggle
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.gyf.barlibrary.ImmersionBar
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.FragmentationActivity
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.entity.TabBean
import com.lyj.fakepivix.databinding.ActivityMainBinding
import com.lyj.fakepivix.module.home.home.HomeFragment
import com.lyj.fakepivix.module.home.news.NewsFragment
import com.lyj.fakepivix.module.home.search.SearchFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_main_content.*

/**
 * @author greensun
 *
 * @date 2019/3/20
 *
 * @desc
 */
class MainActivity : FragmentationActivity<ActivityMainBinding, BaseViewModel<*>?>() {

    override val mViewModel: BaseViewModel<*>? = null

    private val fragments = mutableListOf<FragmentationFragment<*, *>>()

    private var prePosition = 0

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).init()
        val content = mBinding.content
        val toggle = ActionBarDrawerToggle(this, drawerLayout, content.toolbar, 0, 0)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        val tabs =  arrayListOf<CustomTabEntity>(
                TabBean(R.drawable.ic_home, R.drawable.ic_home, getString(R.string.tab_home)),
                TabBean(R.drawable.ic_menu_new_arrival, R.drawable.ic_menu_new_arrival, getString(R.string.tab_news)),
                TabBean(R.drawable.ic_search, R.drawable.ic_search, getString(R.string.tab_search))
        )
        var homeFragment = findFragment(HomeFragment::class.java)
        var newsFragment = findFragment(NewsFragment::class.java)
        var searchFragment = findFragment(SearchFragment::class.java)
        if(homeFragment == null) {
            homeFragment = HomeFragment.newInstance()
        }
        if(newsFragment == null) {
            newsFragment = NewsFragment.newInstance()
        }
        if(searchFragment == null) {
            searchFragment = SearchFragment.newInstance()
        }
        fragments.clear()
        fragments.add(homeFragment)
        fragments.add(newsFragment)
        fragments.add(searchFragment)
        loadMultipleRootFragment(R.id.fragmentContainer, 0, fragments[0], fragments[1], fragments[2])
        content.tabLayout.setTabData(tabs)
        (0 until fragments.size).filterNot {
            it == prePosition
        }.forEach {
            content.tabLayout.getIconView(it).alpha = 0.7f
        }
        tabLayout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                content.tabLayout.getIconView(prePosition).alpha = 0.7f
                content.tabLayout.getIconView(position).alpha = 1f
                showHideFragment(fragments[position], fragments[prePosition])
                when(position) {
                    0 -> mToolbar?.setTitle(R.string.tab_home)
                }
                prePosition = position
            }

            override fun onTabReselect(position: Int) {

            }

        })
    }

    override fun bindLayout(): Int = R.layout.activity_main

}