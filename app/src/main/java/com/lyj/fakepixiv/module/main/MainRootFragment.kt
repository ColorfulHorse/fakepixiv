package com.lyj.fakepixiv.module.main

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.gyf.immersionbar.ImmersionBar
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.base.FragmentationFragment
import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.entity.TabBean
import com.lyj.fakepixiv.app.utils.Router
import com.lyj.fakepixiv.databinding.MainRoot
import com.lyj.fakepixiv.module.main.home.HomeFragment
import com.lyj.fakepixiv.module.main.news.NewsFragment
import com.lyj.fakepixiv.module.main.search.SearchFragment
import com.lyj.fakepixiv.module.main.search.illust.SearchTagFragment

/**
 * @author greensun
 *
 * @date 2019/4/7
 *
 * @desc 主页root fragment
 */
class MainRootFragment : FragmentationFragment<MainRoot, BaseViewModel?>() {
    companion object {
        const val EXTRA_SWITCH_TAB = "EXTRA_SWITCH_TAB"

        fun newInstance(): MainRootFragment {
            val args = Bundle()
            val fragment = MainRootFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override var mViewModel: BaseViewModel? = null

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

//        with(mBinding) {
//
//        }
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
            var category = IllustCategory.OTHER
            val active = Router.getActiveFragment()
            if (active is SearchTagFragment) {
                category = active.category
            }
            Router.goSearch(category)
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