package com.lyj.fakepivix.module.main.news.follow

import android.os.Bundle
import com.flyco.tablayout.listener.CustomTabEntity
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.entity.TabBean
import com.lyj.fakepivix.databinding.FragmentNewsFollowBinding
import com.lyj.fakepivix.module.main.news.follow.illust.FollowIllustFragment
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

    companion object {
        fun newInstance() = NewsFollowFragment()
    }

    override fun init(savedInstanceState: Bundle?) {
        val fragments = arrayListOf<ISupportFragment>(
                FollowIllustFragment.newInstance(),
                FollowIllustFragment.newInstance()
        )
        loadMultipleRootFragment(R.id.fragment_container, 0, fragments[0], fragments[1])
        val tabs = arrayListOf<CustomTabEntity>(
                TabBean(title = getString(R.string.tab_pic)),
                TabBean(title = getString(R.string.tab_novel))
        )
        mBinding.tabLayout.setTabData(tabs)
    }


    override fun immersionBarEnabled(): Boolean = false

    override fun bindLayout(): Int = R.layout.fragment_news_follow

}