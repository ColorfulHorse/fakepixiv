package com.lyj.fakepivix.module.main.news

import android.os.Bundle
import android.support.v4.app.Fragment
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.CommonFragmentAdapter
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.databinding.FragmentNewsBinding
import com.lyj.fakepivix.module.main.news.follow.NewsFollowFragment
import com.lyj.fakepivix.module.main.news.news.NewsNewsFragment
import me.yokeyword.fragmentation.ISupportFragment


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
                NewsFollowFragment.newInstance()
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
        }

    }

    override fun immersionBarEnabled(): Boolean = false

    override fun bindLayout(): Int = R.layout.fragment_news

}