package com.lyj.fakepivix.module.main.search

import android.os.Bundle
import com.flyco.tablayout.listener.CustomTabEntity
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.entity.TabBean
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

    companion object {
        fun newInstance() = SearchFragment()
    }


    override fun init(savedInstanceState: Bundle?) {
        val tabs = arrayListOf<CustomTabEntity>(
                TabBean(title = getString(R.string.tab_pic)),
                TabBean(title = getString(R.string.tab_novel)),
                TabBean(title = getString(R.string.tab_user))
        )
        mBinding.tabLayout.setTabData(tabs)
    }

    override fun immersionBarEnabled(): Boolean = false

    override fun bindLayout(): Int = R.layout.fragment_search

}