package com.lyj.fakepivix.module.login

import android.os.Bundle
import com.gyf.barlibrary.ImmersionBar
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.FragmentationActivity
import com.lyj.fakepivix.databinding.ActivityLoginBinding
import com.lyj.fakepivix.module.login.login.LoginFragment
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator

class LoginActivity : FragmentationActivity<ActivityLoginBinding, WallpaperViewModel>() {

    override var mViewModel: WallpaperViewModel = WallpaperViewModel()

    override fun initData(savedInstanceState: Bundle?) {
        fragmentAnimator = DefaultHorizontalAnimator()
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this)
                .transparentStatusBar()
                .init()
        if (findFragment(LoginFragment::class.java) == null) {
            loadRootFragment(R.id.fragmentContainer, LoginFragment.newInstance())
        }
    }

    override fun bindLayout(): Int = R.layout.activity_login

    override fun onBackPressedSupport() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            pop()
        }
    }
}
