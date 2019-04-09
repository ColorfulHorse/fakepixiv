package com.lyj.fakepivix.module.home

import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.gyf.barlibrary.ImmersionBar
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.entity.TabBean
import com.lyj.fakepivix.databinding.MainRoot
import com.lyj.fakepivix.module.home.home.HomeFragment
import com.lyj.fakepivix.module.home.news.NewsFragment
import com.lyj.fakepivix.module.home.search.SearchFragment

/**
 * @author greensun
 *
 * @date 2019/4/7
 *
 * @desc
 */
class MainRootFragment : FragmentationFragment<MainRoot, BaseViewModel<*>?>() {
    companion object {
        const val EXTRA_SWITCH_TAB = "EXTRA_SWITCH_TAB"

        fun newInstance(): MainRootFragment {
            val args = Bundle()
            val fragment = MainRootFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override var mViewModel: BaseViewModel<*>? = null

    private val fragments = mutableListOf<FragmentationFragment<*, *>>()

    private var prePosition = 0

    // 切换了tab
    var navSelectAction: ((Int) -> Unit)? = null


    override fun init(savedInstanceState: Bundle?) {
        val drawerLayout = mActivity.findViewById<DrawerLayout>(R.id.drawerLayout)

        val toggle = ActionBarDrawerToggle(mActivity, drawerLayout, mBinding.toolbar, 0, 0)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        initFragment()
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
                .titleBarMarginTop(mBinding.contentView)
                .statusBarColor(R.color.transparent)
                .statusBarColorTransform(R.color.black)
                .statusBarAlpha(0.25f)
                .init()
    }

    private fun initFragment() {
        val tabs = arrayListOf<CustomTabEntity>(
                TabBean(R.drawable.ic_home, R.drawable.ic_home, getString(R.string.tab_home)),
                TabBean(R.drawable.ic_menu_new_arrival, R.drawable.ic_menu_new_arrival, getString(R.string.tab_news)),
                TabBean(R.drawable.ic_search, R.drawable.ic_search, getString(R.string.tab_search))
        )
        var homeFragment = findFragment(HomeFragment::class.java)
        var newsFragment = findFragment(NewsFragment::class.java)
        var searchFragment = findFragment(SearchFragment::class.java)
        if (homeFragment == null) {
            homeFragment = HomeFragment.newInstance()
        }
        if (newsFragment == null) {
            newsFragment = NewsFragment.newInstance()
        }
        if (searchFragment == null) {
            searchFragment = SearchFragment.newInstance()
        }
        fragments.clear()
        fragments.add(homeFragment)
        fragments.add(newsFragment)
        fragments.add(searchFragment)
        loadMultipleRootFragment(R.id.fragmentContainer, 0, fragments[0], fragments[1], fragments[2])
        mBinding.tabLayout.setTabData(tabs)
        (0 until fragments.size).filterNot {
            it == prePosition
        }.forEach {
            mBinding.tabLayout.getIconView(it).alpha = 0.7f
        }
        mBinding.tabLayout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                switchTab(position)
                navSelectAction?.let {
                    it(position)
                }
            }

            override fun onTabReselect(position: Int) {

            }

        })
    }

    override fun onNewBundle(args: Bundle) {
        val position = args[EXTRA_SWITCH_TAB] as Int
        mBinding.tabLayout.currentTab = position
        switchTab(position)
    }

    /**
     * 切换tab页
     */
    private fun switchTab(position: Int) {
        mBinding.tabLayout.getIconView(prePosition).alpha = 0.7f
        mBinding.tabLayout.getIconView(position).alpha = 1f
        showHideFragment(fragments[position], fragments[prePosition])
        when (position) {
            0 -> mToolbar?.setTitle(R.string.tab_home)
            1 -> mToolbar?.setTitle(R.string.tab_news)
            2 -> mToolbar?.setTitle(R.string.tab_search)
        }
        prePosition = position
    }

    override fun bindLayout(): Int = R.layout.fragment_main_root
}