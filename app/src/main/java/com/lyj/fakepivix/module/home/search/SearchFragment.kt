package com.lyj.fakepivix.module.home.search

import android.os.Bundle
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.databinding.FragmentSearchBinding
import com.lyj.fakepivix.module.home.news.NewsFragment


/**
 * @author greensun
 *
 * @date 2019/4/3
 *
 * @desc
 */
class SearchFragment : FragmentationFragment<FragmentSearchBinding, BaseViewModel<*>?>() {

    override var mViewModel: BaseViewModel<*>? = null

    companion object {
        fun newInstance() = SearchFragment()
    }


    override fun init(savedInstanceState: Bundle?) {

    }

    override fun immersionBarEnabled(): Boolean = false

    override fun bindLayout(): Int = R.layout.fragment_search

}