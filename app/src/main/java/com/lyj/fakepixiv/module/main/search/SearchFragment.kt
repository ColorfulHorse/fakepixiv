package com.lyj.fakepixiv.module.main.search

import android.os.Bundle
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.base.FragmentationFragment
import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.data.source.remote.UserRepository
import com.lyj.fakepixiv.app.entity.TabBean
import com.lyj.fakepixiv.databinding.FragmentSearchBinding
import com.lyj.fakepixiv.module.common.UserListFragment
import com.lyj.fakepixiv.module.common.UserListViewModel
import com.lyj.fakepixiv.module.main.search.illust.SearchTagFragment
import me.yokeyword.fragmentation.ISupportFragment


/**
 * @author greensun
 *
 * @date 2019/4/3
 *
 * @desc
 */
class SearchFragment : FragmentationFragment<FragmentSearchBinding, BaseViewModel?>() {

    override var mViewModel: BaseViewModel? = null

    companion object {
        fun newInstance() = SearchFragment()
    }


    override fun init(savedInstanceState: Bundle?) {
        val userListFragment = UserListFragment.newInstance()
        val userViewModel = UserListViewModel {
            UserRepository.instance
                    .getRecommendUsers()
        }
        userListFragment.mViewModel = userViewModel
        val fragments = listOf<ISupportFragment>(
                SearchTagFragment.newInstance(IllustCategory.ILLUST),
                SearchTagFragment.newInstance(IllustCategory.NOVEL),
                userListFragment
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