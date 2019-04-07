package com.lyj.fakepivix.module.home

import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.view.Gravity
import com.gyf.barlibrary.ImmersionBar
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.FragmentationActivity
import com.lyj.fakepivix.app.utils.statusBarColor
import com.lyj.fakepivix.databinding.ActivityMainBinding
import me.yokeyword.fragmentation.ISupportFragment


/**
 * @author greensun
 *
 * @date 2019/3/20
 *
 * @desc
 */
class MainActivity : FragmentationActivity<ActivityMainBinding, BaseViewModel<*>?>() {

    override val mViewModel: BaseViewModel<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this).init()
        var rootFragment = findFragment(MainRootFragment::class.java)
        if(rootFragment == null) {
            rootFragment = MainRootFragment.newInstance()
        }
        rootFragment.navSelectAction = {
            val id = when(it) {
                0 -> R.id.nav_home
                1 -> R.id.nav_news
                2 -> R.id.nav_search
                else -> 0
            }
            mBinding.nav.nav.setCheckedItem(id)
        }
        loadRootFragment(R.id.fl_container, rootFragment)
        initNavigationView()
    }


    /**
     * 侧滑
     */
    private fun initNavigationView() {
        arrayOf(R.id.nav_follower, R.id.nav_following, R.id.nav_friend)
                .forEach {
                    val menuItem = mBinding.nav.nav.menu.findItem(it)
                    val title = "\t ${menuItem.title}"
                    menuItem.title = title
                }
        val historyItem = mBinding.nav.nav.menu.findItem(R.id.nav_history)
        val span = ImageSpan(this, R.drawable.ic_profile_premium)
        val sstr = SpannableString("${historyItem.title}         ")
        sstr.setSpan(span, historyItem.title.length+1, sstr.length-1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
        historyItem.title = sstr

        mBinding.nav.nav.setNavigationItemSelectedListener {
            mBinding.drawerLayout.closeDrawer(Gravity.START)
            when (it.itemId) {
                R.id.nav_home, R.id.nav_news, R.id.nav_search -> {
                    var rootFragment = findFragment(MainRootFragment::class.java)
                    if (rootFragment == null) {
                        rootFragment = MainRootFragment.newInstance()
                    }
                    val data = Bundle().apply {
                        putInt(MainRootFragment.EXTRA_SWITCH_TAB, when(it.itemId) {
                            R.id.nav_home -> 0
                            R.id.nav_news -> 1
                            R.id.nav_search -> 2
                            else -> 0
                        })
                    }
                    rootFragment.putNewBundle(data)
                    start(rootFragment, ISupportFragment.SINGLETASK)
                    true
                }
                else -> false
            }
        }
    }

    override fun bindLayout(): Int = R.layout.activity_main

}