package com.lyj.fakepivix.module.main.news.friend


import android.os.Bundle
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.constant.IllustCategory.ILLUSTANDCOMIC
import com.lyj.fakepivix.app.constant.IllustCategory.NOVEL
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.entity.TabBean
import com.lyj.fakepivix.databinding.FragmentNewsFollowBinding
import com.lyj.fakepivix.databinding.FragmentNewsNewsBinding
import com.lyj.fakepivix.module.main.common.IllustListFragment
import com.lyj.fakepivix.module.main.common.IllustListViewModel


/**
 * @author greensun
 *
 * @date 2019/4/3
 *
 * @desc 最新-好P友
 */
class NewsFriendFragment : FragmentationFragment<FragmentNewsNewsBinding, BaseViewModel<*>?>() {

    override var mViewModel: BaseViewModel<*>? = null

    lateinit var illustViewModel: IllustListViewModel
    lateinit var novelViewModel: IllustListViewModel

    val fragments: MutableList<IllustListFragment> = mutableListOf()

    companion object {
        fun newInstance() = NewsFriendFragment()
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
        }
    }

    private fun initSubFragment() {
        val followIllustFragment = IllustListFragment.newInstance(ILLUSTANDCOMIC)
        val followNovelFragment = IllustListFragment.newInstance(NOVEL)

        illustViewModel = IllustListViewModel(ILLUSTANDCOMIC) {
            IllustRepository.instance.loadFriendIllust(ILLUSTANDCOMIC)
        }
        novelViewModel = IllustListViewModel(NOVEL) {
            IllustRepository.instance.loadFriendIllust(NOVEL)
        }
        followIllustFragment.mViewModel = illustViewModel
        followNovelFragment.mViewModel = novelViewModel
        fragments.add(followIllustFragment)
        fragments.add(followNovelFragment)
        loadMultipleRootFragment(R.id.fragment_container, 0, fragments[0], fragments[1])
    }

    override fun immersionBarEnabled(): Boolean = false

    override fun bindLayout(): Int = R.layout.fragment_news_news

}