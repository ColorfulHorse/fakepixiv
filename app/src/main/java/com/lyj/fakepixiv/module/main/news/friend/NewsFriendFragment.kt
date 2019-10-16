package com.lyj.fakepixiv.module.main.news.friend


import android.os.Bundle
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.base.FragmentationFragment
import com.lyj.fakepixiv.app.constant.IllustCategory.OTHER
import com.lyj.fakepixiv.app.constant.IllustCategory.NOVEL
import com.lyj.fakepixiv.app.data.source.remote.IllustRepository
import com.lyj.fakepixiv.app.entity.TabBean
import com.lyj.fakepixiv.databinding.FragmentNewsNewsBinding
import com.lyj.fakepixiv.module.common.IllustListFragment
import com.lyj.fakepixiv.module.common.IllustListViewModel


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
        val followIllustFragment = IllustListFragment.newInstance(OTHER)
        val followNovelFragment = IllustListFragment.newInstance(NOVEL)

        illustViewModel = IllustListViewModel {
            IllustRepository.instance.loadFriendIllust(OTHER)
        }
        novelViewModel = IllustListViewModel {
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