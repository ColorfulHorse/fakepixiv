package com.lyj.fakepivix.module.illust.ranking

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import com.gyf.barlibrary.ImmersionBar
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.CommonFragmentAdapter
import com.lyj.fakepivix.app.base.BackFragment
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.constant.Constant
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.databinding.FragmentRootRankIllustBinding

/**
 * @author greensun
 *
 * @date 2019/3/20
 *
 * @desc
 */
class RankIllustRootFragment : BackFragment<FragmentRootRankIllustBinding, BaseViewModel<IModel?>?>() {
    override var mViewModel: BaseViewModel<IModel?>? = null

    private val fragments = mutableListOf<FragmentationFragment<*,*>>()
    var category = IllustCategory.ILLUST
    companion object {
        const val EXTRA_CATEGORY = "EXTRA_CATEGORY"
        fun newInstance(@IllustCategory category: String) = RankIllustRootFragment().apply {
            arguments = Bundle().apply {
                putString(RankIllustFragment.EXTRA_CATEGORY, category)
            }
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        arguments?.let {
            category = it.getString(RankIllustFragment.EXTRA_CATEGORY, IllustCategory.ILLUST)
        }
        mToolbar?.title = getString(R.string.ranking)
        val titles = when(category) {
            IllustCategory.ILLUST -> resources.getStringArray(R.array.illust_rank_modes)
            IllustCategory.COMIC -> resources.getStringArray(R.array.comic_rank_modes)
            IllustCategory.NOVEL -> resources.getStringArray(R.array.novel_rank_modes)
            else -> resources.getStringArray(R.array.illust_rank_modes)
        }
        initFragments()
        with(mBinding) {
            viewPager.adapter = CommonFragmentAdapter(childFragmentManager, fragments, titles)
            tabLayout.setupWithViewPager(viewPager)
        }
    }

    private fun initFragments() {
        val modes = when(category) {
            IllustCategory.ILLUST -> Constant.Net.ILLUST_RANK_MODES
            IllustCategory.COMIC -> Constant.Net.COMIC_RANK_MODES
            IllustCategory.NOVEL -> Constant.Net.NOVEL_RANK_MODES
            else -> Constant.Net.ILLUST_RANK_MODES
        }
        val realCategory = if (category == IllustCategory.NOVEL) IllustCategory.NOVEL else IllustCategory.ILLUST
        for ((index, mode) in modes.withIndex()) {
            val fragment = if (index == modes.size - 1) {
                RankIllustFragment.newInstance(category, true)
            }else {
                RankIllustFragment.newInstance(category)
            }
            val viewModel = RankIllustViewModel(realCategory, mode)
            fragment.mViewModel = viewModel
            fragments.add(fragment)
        }
    }


    override fun initImmersionBar() {
        ImmersionBar.with(this)
                .titleBarMarginTop(mBinding.contentView)
                .statusBarColor(R.color.transparent, R.color.black, 0.25f)
                .statusBarAlpha(0.25f)
                .init()
    }

    override fun bindBackIcon(): Drawable {
        return DrawerArrowDrawable(mActivity).apply {
            progress = 1F
            color = Color.WHITE
        }
    }

    override fun bindLayout(): Int = R.layout.fragment_root_rank_illust
}