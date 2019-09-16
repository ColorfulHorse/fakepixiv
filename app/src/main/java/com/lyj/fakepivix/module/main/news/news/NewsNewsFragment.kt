package com.lyj.fakepivix.module.main.news.news

import android.os.Bundle
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.constant.IllustCategory.*


import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.entity.TabBean
import com.lyj.fakepivix.databinding.FragmentNewsNewsBinding
import com.lyj.fakepivix.module.common.IllustListFragment
import com.lyj.fakepivix.module.common.IllustListViewModel
import kotlinx.coroutines.rx2.awaitFirst
import me.yokeyword.fragmentation.ISupportFragment


/**
 * @author greensun
 *
 * @date 2019/4/3
 *
 * @desc 最新-最新
 */
class NewsNewsFragment : FragmentationFragment<FragmentNewsNewsBinding, BaseViewModel<*>?>() {

    override var mViewModel: BaseViewModel<*>? = null

    private lateinit var illustViewModel: IllustListViewModel
    private lateinit var comicViewModel: IllustListViewModel
    private lateinit var novelViewModel: IllustListViewModel

    companion object {
        fun newInstance() = NewsNewsFragment()
    }

    override fun init(savedInstanceState: Bundle?) {
        val illustFragment = IllustListFragment.newInstance(ILLUST)
        val comicFragment = IllustListFragment.newInstance(COMIC)
        val novelFragment = IllustListFragment.newInstance(NOVEL)
        illustViewModel = IllustListViewModel {
            IllustRepository.instance.loadNewIllust(ILLUST)
        }
        comicViewModel = IllustListViewModel {
            IllustRepository.instance.loadNewIllust(COMIC)
        }
        novelViewModel = IllustListViewModel {
            IllustRepository.instance.loadNewIllust(NOVEL)
        }
        illustFragment.mViewModel = illustViewModel
        comicFragment.mViewModel = comicViewModel
        novelFragment.mViewModel = novelViewModel

        val fragments = arrayListOf<ISupportFragment>(illustFragment, comicFragment, novelFragment)
        loadMultipleRootFragment(R.id.fragment_container, 0, fragments[0], fragments[1], fragments[2])
        val tabs = arrayListOf<CustomTabEntity>(
                TabBean(title = getString(R.string.tab_illust)),
                TabBean(title = getString(R.string.tab_comic)),
                TabBean(title = getString(R.string.tab_novel))
        )
        with(mBinding) {
            tabLayout.setTabData(tabs)
            tabLayout.currentTab = 0
            tabLayout.setOnTabSelectListener(object : OnTabSelectListener {
                override fun onTabSelect(position: Int) {
                    showHideFragment(fragments[position])
                }

                override fun onTabReselect(position: Int) {

                }

            })
        }
    }

    override fun immersionBarEnabled(): Boolean = false

    override fun bindLayout(): Int = R.layout.fragment_news_news

}