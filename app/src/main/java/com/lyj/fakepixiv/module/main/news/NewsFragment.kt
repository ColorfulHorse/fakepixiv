package com.lyj.fakepixiv.module.main.news

import android.os.Bundle
import android.support.v4.app.Fragment
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.adapter.CommonFragmentAdapter
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.base.FragmentationFragment
import com.lyj.fakepixiv.databinding.FragmentNewsBinding
import com.lyj.fakepixiv.module.main.news.follow.NewsFollowFragment
import com.lyj.fakepixiv.module.main.news.friend.NewsFriendFragment
import com.lyj.fakepixiv.module.main.news.news.NewsNewsFragment


/**
 * @author greensun
 *
 * @date 2019/4/3
 *
 * @desc
 */
class NewsFragment : FragmentationFragment<FragmentNewsBinding, BaseViewModel<*>?>() {

    override var mViewModel: BaseViewModel<*>? = null

    companion object {
        fun newInstance() = NewsFragment()
    }

    override fun init(savedInstanceState: Bundle?) {
        val fragments = listOf<Fragment>(
                NewsFollowFragment.newInstance(),
                NewsNewsFragment.newInstance(),
                NewsFriendFragment.newInstance()
        )
        val titles = arrayOf(
                getString(R.string.tab_follower),
                getString(R.string.tab_news),
                getString(R.string.tab_friend)
                )
        with(mBinding) {
            viewPager.offscreenPageLimit = 2
            viewPager.adapter = CommonFragmentAdapter(childFragmentManager, fragments, titles)
            tabLayout.setupWithViewPager(viewPager)
            tabLayout.tabIndicatorGravity
        }

    }

    override fun immersionBarEnabled(): Boolean = false

    override fun bindLayout(): Int = R.layout.fragment_news

}