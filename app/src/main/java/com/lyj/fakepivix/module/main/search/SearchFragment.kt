package com.lyj.fakepivix.module.main.search

import android.os.Bundle
import android.support.v4.app.Fragment
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.entity.TabBean
import com.lyj.fakepivix.databinding.FragmentSearchBinding
import com.lyj.fakepivix.module.main.search.illust.SearchIllustFragment
import me.yokeyword.fragmentation.ISupportFragment


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
        val fragments = listOf<ISupportFragment>(
                SearchIllustFragment.newInstance(IllustCategory.ILLUST),
                SearchIllustFragment.newInstance(IllustCategory.NOVEL),
                SearchIllustFragment.newInstance(IllustCategory.ILLUST)
        )
        loadMultipleRootFragment(R.id.fl_container, 0, fragments[0], fragments[1], fragments[2])
        val tabs = arrayListOf<CustomTabEntity>(
                TabBean(title = getString(R.string.tab_pic)),
                TabBean(title = getString(R.string.tab_novel)),
                TabBean(title = getString(R.string.tab_user))
        )
        with(mBinding){
            tabLayout.setTabData(tabs)
            tabLayout.currentTab = 0
            tabLayout.setOnTabSelectListener(object : OnTabSelectListener {
                override fun onTabSelect(position: Int) {
                    val fragment = fragments[position]
                    showHideFragment(fragment)
                }
                override fun onTabReselect(position: Int) {

                }

            })
        }
    }

    override fun immersionBarEnabled(): Boolean = false

    override fun bindLayout(): Int = R.layout.fragment_search

}