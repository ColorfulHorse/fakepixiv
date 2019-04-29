package com.lyj.fakepivix.module.main.news

import android.os.Bundle
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.databinding.FragmentNewsBinding


/**
 * @author greensun
 *
 * @date 2019/4/3
 *
 * @desc
 */
class NewsFragment : FragmentationFragment<FragmentNewsBinding, BaseViewModel<*>?>() {

    override var mViewModel: BaseViewModel<*>? = null

    companion object {
        fun newInstance() = NewsFragment()
    }

    override fun init(savedInstanceState: Bundle?) {
        mBinding.tabLayout.addTab(mBinding.tabLayout.newTab().setText(R.string.subtab_follower), true)
        mBinding.tabLayout.addTab(mBinding.tabLayout.newTab().setText(R.string.subtab_news))
        mBinding.tabLayout.addTab(mBinding.tabLayout.newTab().setText(R.string.subtab_friend))
        with(mBinding) {

        }

    }

    override fun immersionBarEnabled(): Boolean = false

    override fun bindLayout(): Int = R.layout.fragment_news

}