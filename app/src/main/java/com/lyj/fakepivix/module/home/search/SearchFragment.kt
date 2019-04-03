package com.lyj.fakepivix.module.home.search

import android.os.Bundle
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.databinding.FragmentSearchBinding


/**
 * @author greensun
 *
 * @date 2019/4/3
 *
 * @desc
 */
class SearchFragment : FragmentationFragment<FragmentSearchBinding, BaseViewModel<*>?>() {

    override var mViewModel: BaseViewModel<*>? = null

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun bindLayout(): Int = R.layout.fragment_search

}