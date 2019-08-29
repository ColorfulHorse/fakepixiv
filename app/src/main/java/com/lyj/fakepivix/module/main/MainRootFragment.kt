package com.lyj.fakepivix.module.main

import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.View
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.gyf.barlibrary.ImmersionBar
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.entity.TabBean
import com.lyj.fakepivix.app.utils.Router
import com.lyj.fakepivix.databinding.MainRoot
import com.lyj.fakepivix.module.main.home.HomeFragment
import com.lyj.fakepivix.module.main.news.NewsFragment
import com.lyj.fakepivix.module.main.search.SearchFragment
import com.lyj.fakepivix.module.main.search.illust.SearchTagFragment
import com.lyj.fakepivix.module.main.search.main.SearchMainFragment

/**
 * @author greensun
 *
 * @date 2019/4/7
 *
 * @desc 主页root fragment
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
        var homeFragment = findChildFragment(HomeFragment::class.java)
        var newsFragment = findChildFragment(NewsFragment::class.java)
        var searchFragment = findChildFragment(SearchFragment::class.java)
        if (homeFragment == null) {
            homeFragment = HomeFragment.newInstance()
            newsFragment = NewsFragment.newInstance()
            searchFragment = SearchFragment.newInstance()
            loadMultipleRootFragment(R.id.fragmentContainer, 0, homeFragment, newsFragment, searchFragment)
        }

        fragments.clear()
        fragments.add(homeFragment)
        newsFragment?.let { fragments.add(it) }
        searchFragment?.let { fragments.add(it) }

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
        mBinding.search.setOnClickListener {
            var category = IllustCategory.ILLUST
            val active = Router.getActiveFragment()
            if (active is SearchTagFragment) {
                category = active.category
                start(SearchMainFragment.newInstance(category))
            }
        }
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
        mBinding.search.visibility = View.GONE
        when (position) {
            0 -> mToolbar?.setTitle(R.string.tab_home)
            1 -> mToolbar?.setTitle(R.string.tab_news)
            2 -> {
                mBinding.search.visibility = View.VISIBLE
                mToolbar?.title = ""
            }
        }
        prePosition = position
    }

    override fun bindLayout(): Int = R.layout.fragment_main_root
}