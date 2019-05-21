package com.lyj.fakepivix.module.main.news.follow

import android.os.Bundle
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.constant.ILLUST
import com.lyj.fakepivix.app.constant.ILLUSTANDCOMIC
import com.lyj.fakepivix.app.constant.NOVEL
import com.lyj.fakepivix.app.data.model.response.IllustListResp
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.entity.TabBean
import com.lyj.fakepivix.app.utils.attachHeader
import com.lyj.fakepivix.databinding.FragmentNewsFollowBinding
import com.lyj.fakepivix.module.main.common.IllustListFragment
import com.lyj.fakepivix.module.main.common.IllustListViewModel
import io.reactivex.Observable
import me.yokeyword.fragmentation.ISupportFragment


/**
 * @author greensun
 *
 * @date 2019/4/3
 *
 * @desc
 */
class NewsFollowFragment : FragmentationFragment<FragmentNewsFollowBinding, BaseViewModel<*>?>() {

    override var mViewModel: BaseViewModel<*>? = null

    lateinit var illustViewModel: IllustListViewModel
    lateinit var novelViewModel: IllustListViewModel

    companion object {
        fun newInstance() = NewsFollowFragment()
    }

    override fun init(savedInstanceState: Bundle?) {
        val followIllustFragment = IllustListFragment.newInstance(ILLUSTANDCOMIC)
        val followNovelFragment = IllustListFragment.newInstance(NOVEL)
        illustViewModel = object : IllustListViewModel(ILLUSTANDCOMIC) {
            override fun getIllustList(): Observable<IllustListResp> = IllustRepository.instance.loadFollowedIllust(ILLUST)
        }
        novelViewModel = object : IllustListViewModel(NOVEL) {
            override fun getIllustList(): Observable<IllustListResp> = IllustRepository.instance.loadFollowedIllust(NOVEL)
        }
        followIllustFragment.mViewModel = illustViewModel
        followNovelFragment.mViewModel = novelViewModel

        val fragments = arrayListOf<ISupportFragment>(followIllustFragment, followNovelFragment)
        loadMultipleRootFragment(R.id.fragment_container, 0, fragments[0], fragments[1])
        val tabs = arrayListOf<CustomTabEntity>(
                TabBean(title = getString(R.string.tab_pic)),
                TabBean(title = getString(R.string.tab_novel))
        )
        with(mBinding) {
            tabLayout.setTabData(tabs)
            tabLayout.setOnTabSelectListener(object : OnTabSelectListener {
                override fun onTabSelect(position: Int) {
                    showHideFragment(fragments[position])
                }

                override fun onTabReselect(position: Int) {

                }

            })
            followIllustFragment.initializer = {
               // followIllustFragment.getRecyclerView().attachHeader(header)
            }
        }
    }

    override fun immersionBarEnabled(): Boolean = false

    override fun bindLayout(): Int = R.layout.fragment_news_follow

}