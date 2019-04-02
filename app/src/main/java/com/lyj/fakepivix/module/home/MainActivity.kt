package com.lyj.fakepivix.module.home

import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.FragmentationActivity
import com.lyj.fakepivix.databinding.ActivityMainBinding
import com.lyj.fakepivix.module.sample.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author greensun
 *
 * @date 2019/3/20
 *
 * @desc
 */
class MainActivity : FragmentationActivity<ActivityMainBinding, MainViewModel>() {

    override var mViewModel: MainViewModel = MainViewModel()

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun initView(savedInstanceState: Bundle?) {
        val content = mBinding.content
        val toggle = ActionBarDrawerToggle(this, drawerLayout, content.toolbar, 0, 0)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun bindLayout(): Int = R.layout.activity_main

}