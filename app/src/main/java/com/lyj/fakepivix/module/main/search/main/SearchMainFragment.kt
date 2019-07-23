package com.lyj.fakepivix.module.main.search.main

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.CommonFragmentAdapter
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.entity.TabBean
import com.lyj.fakepivix.databinding.FragmentSearchMainBinding
import com.lyj.fakepivix.module.main.common.IllustListFragment


/**
 * @author greensun
 *
 * @date 2019/4/3
 *
 * @desc
 */
class SearchMainFragment : FragmentationFragment<FragmentSearchMainBinding, SearchMainViewModel>() {

    override var mViewModel: SearchMainViewModel = SearchMainViewModel()

    private var category = IllustCategory.OTHER

    companion object {
        private const val EXTRA_CATEGORY = "EXTRA_CATEGORY"
        fun newInstance(@IllustCategory category: String? = null) = SearchMainFragment().apply {
            category?.let {
                arguments = Bundle().apply {
                    putString(EXTRA_CATEGORY, category)
                }
            }
        }
    }


    override fun init(savedInstanceState: Bundle?) {
        arguments?.let {
            category = it.getString(EXTRA_CATEGORY, IllustCategory.OTHER)
            mViewModel.category = category
        }

        val newFragment = IllustListFragment.newInstance(category)
        val popularFragment = IllustListFragment.newInstance(category)
        val descFragment = IllustListFragment.newInstance(category)
        val fragments = listOf(newFragment, popularFragment, descFragment)

        val tabs = arrayListOf<CustomTabEntity>(
                TabBean(title = getString(R.string.tab_pic)),
                TabBean(title = getString(R.string.tab_novel)),
                TabBean(title = getString(R.string.tab_user))
        )
        val span = ImageSpan(context, R.drawable.ic_profile_premium)
        val sstr = SpannableString(getString(R.string.tab_popular))
        sstr.setSpan(span, 0, 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        val titles = arrayOf(
                getString(R.string.tab_new_works),
                sstr,
                getString(R.string.tab_desc)
        )
        with(mBinding) {
            segmentLayout.setTabData(tabs)
            segmentLayout.setOnTabSelectListener(object : OnTabSelectListener {
                override fun onTabSelect(position: Int) {
                    category = when(position) {
                        0 -> IllustCategory.OTHER
                        1 -> IllustCategory.NOVEL
                        else -> ""
                    }
                    mViewModel.category = category
                }

                override fun onTabReselect(position: Int) {

                }

            })
            viewPager.adapter = CommonFragmentAdapter(childFragmentManager, fragments, titles)
            tabLayout.setupWithViewPager(viewPager)
        }
    }

    override fun immersionBarEnabled(): Boolean = false

    override fun bindLayout(): Int = R.layout.fragment_search_main

}