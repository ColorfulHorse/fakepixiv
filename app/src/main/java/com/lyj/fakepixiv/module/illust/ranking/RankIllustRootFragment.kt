package com.lyj.fakepixiv.module.illust.ranking

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import com.gyf.barlibrary.ImmersionBar
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.adapter.CommonFragmentAdapter
import com.lyj.fakepixiv.app.base.BackFragment
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.base.FragmentationFragment

import com.lyj.fakepixiv.app.constant.Constant
import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.databinding.FragmentRootRankIllustBinding
import java.util.*

/**
 * @author greensun
 *
 * @date 2019/3/20
 *
 * @desc 排行榜根页面
 */
class RankIllustRootFragment : BackFragment<FragmentRootRankIllustBinding, BaseViewModel?>() {
    override var mViewModel: BaseViewModel? = null

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
            category = it.getString(RankIllustRootFragment.EXTRA_CATEGORY, IllustCategory.ILLUST)
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
        for ((index, mode) in modes.withIndex()) {
            val viewModel = RankIllustViewModel(category, mode)
            val fragment = if (index == modes.size - 1) {
                // 过去排行榜
                val c = Calendar.getInstance()
                var y = c.get(Calendar.YEAR)
                var m = c.get(Calendar.MONTH)
                var d = c.get(Calendar.DAY_OF_MONTH)
                if (d <= 1) {
                    if (m <= 0) {
                        c.roll(Calendar.YEAR, -1)
                    }
                    c.roll(Calendar.MONTH, -1)
                }
                c.roll(Calendar.DAY_OF_MONTH, -1)
                y = c.get(Calendar.YEAR)
                m = c.get(Calendar.MONTH)
                d = c.get(Calendar.DAY_OF_MONTH)
                viewModel.date = String.format("%d-%02d-%02d", y, m + 1, d)
                RankIllustFragment.newInstance(category, true)
            }else {
                RankIllustFragment.newInstance(category)
            }
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