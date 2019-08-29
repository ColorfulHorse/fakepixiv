package com.lyj.fakepivix.module.illust

import android.os.Bundle
import android.support.v4.view.ViewPager
import com.gyf.barlibrary.ImmersionBar
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.BackFragment
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.data.model.response.Illust
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
    var key = -1

    var data : List<Illust> = listOf()

    companion object {
        private const val EXTRA_POSITION = "EXTRA_POSITION"
        private const val EXTRA_KEY = "EXTRA_KEY"
        fun newInstance(position: Int, key: Int): IllustDetailRootFragment {
            return IllustDetailRootFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_POSITION, position)
                    putInt(EXTRA_KEY, key)
                }
            }
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        arguments?.let {
            key = it.getInt(EXTRA_KEY, -1)
            position = it.getInt(EXTRA_POSITION, -1)
            data = IllustRepository.instance[key]
        }
        mToolbar?.let {
            //it.overflowIcon = ContextCompat.getDrawable(mActivity, R.drawable.ic_more)
            it.inflateMenu(R.menu.menu_detail_illust)
            it.setOnMenuItemClickListener { menu ->
                when (menu.itemId) {
                    R.id.share -> {}
                    R.id.filter -> {}
                    else -> {}
                }
                true
            }
        }
        val adapter = IllustPagerAdapter(data, childFragmentManager, key)
        with(mBinding) {
            viewPager.adapter = adapter
            viewPager.offscreenPageLimit = 2
            viewPager.setCurrentItem(position, false)
            //viewPager.currentItem = position
            viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(p0: Int) {

                }

                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

                }

                override fun onPageSelected(position: Int) {
                    val fragment = adapter.getFragment(position)
                    vm = fragment?.mViewModel
                }

            })
            val fragment = adapter.getFragment(position)
            if (fragment != null) {
                vm = fragment.mViewModel
            }else {
                // 未初始化则没办法马上拿到
                adapter.getFragment(position) {
                    vm = it.mViewModel
                }
            }
        }
    }

    override fun onDestroyView() {
        if (!diffOrientation) {
            IllustRepository.instance - key
        }
        super.onDestroyView()
    }


    override fun initImmersionBar() {
        ImmersionBar.with(this)
                .titleBar(mBinding.toolbarWrapper)
                .statusBarDarkFont(true)
                .transparentStatusBar()
                .init()
//        ImmersionBar.with(this)
//                .titleBarMarginTop(mBinding.toolbarWrapper)
//                .statusBarColor(R.color.transparent)
//                .statusBarColorTransform(R.color.black)
//                .statusBarAlpha(0.25f)
//                .init()
    }

    override fun bindLayout(): Int = R.layout.fragment_illust_detail_root
}