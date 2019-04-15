package com.lyj.fakepivix.module.main.home.noval

import android.os.Bundle
import com.flyco.tablayout.listener.CustomTabEntity
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.FragmentationFragment;
import com.lyj.fakepivix.app.entity.TabBean
import com.lyj.fakepivix.databinding.CommonRefreshList
import com.lyj.fakepivix.databinding.FragmentHomeBinding


/**
 * @author greensun
 *
 * @date 2019/4/3
 *
 * @desc
 */
class HomeNovalFragment : FragmentationFragment<CommonRefreshList, BaseViewModel<*>?>() {

    override var mViewModel: BaseViewModel<*>? = null

    companion object {
        fun newInstance() = HomeNovalFragment()
    }


    override fun init(savedInstanceState: Bundle?) {

    }

    private fun initFragment() {

    }

    override fun immersionBarEnabled(): Boolean = false

    override fun bindLayout(): Int = R.layout.layout_common_recycler

}