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

    override fun initData(savedInstanceState: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initView(savedInstanceState: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun bindLayout(): Int = R.layout.fragment_home

}