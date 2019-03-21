package com.lyj.fakepivix.module.login

import android.os.Bundle
import com.gyf.barlibrary.ImmersionBar
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingAdapter
import com.lyj.fakepivix.app.base.FragmentationActivity
import com.lyj.fakepivix.app.model.response.Illust
import com.lyj.fakepivix.databinding.ActivityLoginBinding
import com.lyj.fakepivix.databinding.ItemWallpaperBinding
import com.lyj.fakepivix.module.login.login.LoginFragment
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : FragmentationActivity<ActivityLoginBinding, WallpaperViewModel>() {

    override var mViewModel: WallpaperViewModel = WallpaperViewModel()

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this)
                .transparentStatusBar()
                .init()
        if (findFragment(LoginFragment::class.java) == null) {
            loadRootFragment(R.id.fragmentContainer, LoginFragment.newInstance())
        }

        recyclerView.adapter = object: BaseBindingAdapter<Illust, BaseBindingAdapter.BaseBindingViewHolder<ItemWallpaperBinding>>(R.layout.item_wallpaper, mViewModel.data) {
            override fun convert(helper: BaseBindingViewHolder<ItemWallpaperBinding>?, item: Illust?) {
                helper?.binding
            }
        }
    }

    override fun bindLayout(): Int = R.layout.activity_login

    override fun onBackPressedSupport() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            pop()
        }
    }
}
