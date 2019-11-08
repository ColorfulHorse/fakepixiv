package com.lyj.fakepixiv.module.main.news.follow

import android.os.Bundle
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.FragmentationFragment
import com.lyj.fakepixiv.app.constant.Constant
import com.lyj.fakepixiv.app.constant.IllustCategory.*
import com.lyj.fakepixiv.app.constant.Restrict


import com.lyj.fakepixiv.app.data.source.remote.IllustRepository
import com.lyj.fakepixiv.app.entity.TabBean
import com.lyj.fakepixiv.app.utils.SPUtil
import com.lyj.fakepixiv.databinding.FragmentNewsFollowBinding
import com.lyj.fakepixiv.module.common.IllustListFragment
import com.lyj.fakepixiv.module.common.IllustListViewModel


/**
 * @author greensun
 *
 * @date 2019/4/3
 *
 * @desc 最新-关注者
 */
class NewsFollowFragment : FragmentationFragment<FragmentNewsFollowBinding, NewsFollowViewModel>() {

    override var mViewModel: NewsFollowViewModel = NewsFollowViewModel()

    lateinit var illustViewModel: IllustListViewModel
    lateinit var novelViewModel: IllustListViewModel

    val fragments: MutableList<IllustListFragment> = mutableListOf()

    companion object {
        fun newInstance() = NewsFollowFragment()
    }

    override fun init(savedInstanceState: Bundle?) {
        initSubFragment()
        val tabs = arrayListOf<CustomTabEntity>(
                TabBean(title = getString(R.string.tab_pic)),
                TabBean(title = getString(R.string.tab_novel))
        )
        with(mBinding) {
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
            restrict.setOnClickListener {
                val category = when(tabLayout.currentTab) {
                    0 -> OTHER
                    else -> NOVEL
                }
                val dialog = RestrictDialog()
                dialog.category = category
                dialog.onRestrict = {
                    // 筛选当前fragment 列表数据
                    val vm = fragments[tabLayout.currentTab].mViewModel
                    vm?.action = {
                        IllustRepository.instance.loadFollowedIllust(ILLUST, it)
                    }
                    vm?.load()
                }
                fragmentManager?.let { fm -> dialog.show(fm, "RestrictDialog") }
            }
            userHeader.vm = mViewModel.userViewModel
        }
    }

    private fun initSubFragment() {
        val followIllustFragment = IllustListFragment.newInstance(OTHER)
        val followNovelFragment = IllustListFragment.newInstance(NOVEL)

        val illustRestrict = SPUtil.get(Constant.SP.KEY_RESTRICT_ILLUST, Restrict.PUBLIC)
        val novelRestrict = SPUtil.get(Constant.SP.KEY_RESTRICT_NOVEL, Restrict.PUBLIC)

        illustViewModel = IllustListViewModel {
            IllustRepository.instance.loadFollowedIllust(ILLUST, illustRestrict)
        }
        novelViewModel = IllustListViewModel {
            IllustRepository.instance.loadFollowedIllust(NOVEL, novelRestrict)
        }
        followIllustFragment.mViewModel = illustViewModel
        followNovelFragment.mViewModel = novelViewModel
        fragments.add(followIllustFragment)
        fragments.add(followNovelFragment)
        loadMultipleRootFragment(R.id.fragment_container, 0, fragments[0], fragments[1])
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        mViewModel.userViewModel.load()
    }

    override fun immersionBarEnabled(): Boolean = false

    override fun bindLayout(): Int = R.layout.fragment_news_follow

}