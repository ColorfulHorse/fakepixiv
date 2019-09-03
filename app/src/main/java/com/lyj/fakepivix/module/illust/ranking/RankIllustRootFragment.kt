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
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.databinding.FragmentRootRankIllustBinding
import com.lyj.fakepivix.module.common.IllustListViewModel

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

    companion object {
        fun newInstance() = RankIllustRootFragment()
    }

    override fun init(savedInstanceState: Bundle?) {
        mToolbar?.title = getString(R.string.ranking)
        val modes = resources.getStringArray(R.array.rank_modes)
        for (mode in Constant.Net.ILLUST_RANK_MODES) {
            val fragment = RankIllustFragment.newInstance()
            val viewModel = IllustListViewModel {
                IllustRepository.instance
                        .getRankIllust(mode)
            }
            fragment.mViewModel = viewModel
            fragments.add(fragment)
        }
        with(mBinding) {
            viewPager.adapter = CommonFragmentAdapter(childFragmentManager, fragments, modes)
            tabLayout.setupWithViewPager(viewPager)
        }
        initFragment()
    }



    private fun initFragment() {

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