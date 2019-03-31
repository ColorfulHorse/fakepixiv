package com.lyj.fakepivix.module.home

import android.os.Bundle
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.databinding.ActivityMainBinding
import com.lyj.fakepivix.module.sample.MainViewModel

/**
 * @author greensun
 *
 * @date 2019/3/20
 *
 * @desc
 */
class MainActivity : FragmentationFragment<ActivityMainBinding, MainViewModel>() {

    override var mViewModel: MainViewModel = MainViewModel()

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun initView(savedInstanceState: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun bindLayout(): Int = R.layout.activity_main

}