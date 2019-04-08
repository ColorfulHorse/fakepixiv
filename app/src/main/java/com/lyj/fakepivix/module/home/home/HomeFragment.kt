package com.lyj.fakepivix.module.home.home

import android.os.Bundle
import com.flyco.tablayout.listener.CustomTabEntity
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.FragmentationFragment;
import com.lyj.fakepivix.app.entity.TabBean
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


    override fun init(savedInstanceState: Bundle?) {
        val tabs = arrayListOf<CustomTabEntity>(
                TabBean(title = getString(R.string.subtab_illust)),
                TabBean(title = getString(R.string.subtab_comic)),
                TabBean(title = getString(R.string.subtab_novel))
                )
        mBinding.tabLayout.setTabData(tabs)
    }

    override fun immersionBarEnabled(): Boolean = false

    override fun bindLayout(): Int = R.layout.fragment_home

}