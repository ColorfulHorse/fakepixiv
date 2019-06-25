package com.lyj.fakepivix.module.main.illust

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.gyf.barlibrary.ImmersionBar
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.BackFragment
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.databinding.FragmentIllustDetailRootBinding

/**
 * @author greensun
 *
 * @date 2019/5/30
 *
 * @desc 作品详情
 */
class IllustDetailRootFragment : BackFragment<FragmentIllustDetailRootBinding, BaseViewModel<*>?>() {

    override var mViewModel: BaseViewModel<*>? = null

    var position = -1

    companion object {
        private const val EXTRA_POSITION = "EXTRA_POSITION"
        fun newInstance(position: Int): IllustDetailRootFragment {
            return IllustDetailRootFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_POSITION, position)
                }
            }
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        arguments?.let {
            position = it.getInt(EXTRA_POSITION, -1)
        }
        mToolbar?.let {
            it.overflowIcon = ContextCompat.getDrawable(mActivity, R.drawable.ic_more)
            it.inflateMenu(R.menu.menu_detail_toolbar)
            it.setOnMenuItemClickListener { menu ->
                when (menu.itemId) {
                    R.id.share -> {}
                    R.id.filter -> {}
                    else -> {}
                }
                true
            }
        }
        val adapter = IllustPagerAdapter(IllustRepository.instance.illustList, childFragmentManager)
        with(mBinding) {
            viewPager.adapter = adapter
            viewPager.offscreenPageLimit = 2
            viewPager.currentItem = position
            viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(p0: Int) {

                }

                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

                }

                override fun onPageSelected(position: Int) {
                    val fragment = adapter.getFragment(position)
                    vm = fragment?.mViewModel
                    Log.e("xxx", "onPageSelected+: $position fragment${fragment == null}")
                }

            })
            adapter.getFragment(position) {
                vm = it.mViewModel
            }
        }
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
                .titleBar(mBinding.toolbarWrapper)
                .transparentStatusBar()
                .init()
    }

    override fun bindLayout(): Int = R.layout.fragment_illust_detail_root
}