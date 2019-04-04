package com.lyj.fakepivix.module.home.home

import android.os.Bundle
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.FragmentationFragment;
import com.lyj.fakepivix.databinding.FragmentHomeBinding


/**
 * @author greensun
 *
 * @date 2019/4/3
 *
 * @desc
 */
class HomeFragment : FragmentationFragment<FragmentHomeBinding, BaseViewModel<*>?>() {

    override var mViewModel: BaseViewModel<*>? = null

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun bindLayout(): Int = R.layout.fragment_home

}