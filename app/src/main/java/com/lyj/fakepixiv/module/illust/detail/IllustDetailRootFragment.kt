package com.lyj.fakepixiv.module.illust.detail

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import android.view.ViewGroup
import android.view.WindowManager
import com.gyf.immersionbar.ImmersionBar
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.BackFragment
import com.lyj.fakepixiv.app.constant.EXTRA_ID
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.data.source.remote.IllustRepository
import com.lyj.fakepixiv.app.databinding.onPropertyChangedCallback
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.app.utils.doOnPropertyChanged
import com.lyj.fakepixiv.databinding.FragmentIllustDetailRootBinding
import kotlinx.android.synthetic.main.layout_error.view.*

/**
 * @author greensun
 *
 * @date 2019/5/30
 *
 * @desc 作品详情
 */
class IllustDetailRootFragment : BackFragment<FragmentIllustDetailRootBinding, IllustDetailViewModel?>() {

    override var mViewModel: IllustDetailViewModel? = null

    var position = -1
    var key = -1

    var data: List<Illust> = listOf()
    lateinit var adapter: IllustPagerAdapter

    companion object {
        private const val EXTRA_POSITION = "EXTRA_POSITION"
        private const val EXTRA_KEY = "EXTRA_KEY"
        fun newInstance(position: Int = 0, key: Int = -1, illustId: Long = -1): IllustDetailRootFragment {
            return IllustDetailRootFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_POSITION, position)
                    putInt(EXTRA_KEY, key)
                    putLong(EXTRA_ID, illustId)
                }
            }
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        arguments?.let {
            key = it.getInt(EXTRA_KEY, -1)
            position = it.getInt(EXTRA_POSITION, 0)
            val illustId = it.getLong(EXTRA_ID, -1)
            if (key != -1) {
                data = IllustRepository.instance[key]
            } else {
                data = mutableListOf(Illust(id = illustId))
            }
        }
        mToolbar?.let {
            //it.overflowIcon = ContextCompat.getDrawable(mActivity, R.drawable.ic_more)
            it.inflateMenu(R.menu.menu_detail_illust)
            it.setOnMenuItemClickListener { menu ->
                when (menu.itemId) {
                    R.id.share -> {
                    }
                    R.id.filter -> {
                    }
                    else -> {
                    }
                }
                true
            }
        }
        adapter = IllustPagerAdapter(data, childFragmentManager, key)
        with(mBinding) {
            val viewPager = ViewPager(mActivity)
            viewPager.id = R.id.viewPager
            viewPager.layoutParams = ViewGroup.LayoutParams(-1, -1)
            container.addView(viewPager, 0)
            viewPager.adapter = adapter
            viewPager.setCurrentItem(position, false)
            viewPager.offscreenPageLimit = 1
            //viewPager.currentItem = position
            viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(p0: Int) {

                }

                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

                }

                override fun onPageSelected(position: Int) {
                    val fragment = adapter.getFragment(position)
                    vm = fragment?.mViewModel
                    mViewModel = vm
                }

            })
            val fragment = adapter.getFragment(position)
            if (fragment != null) {
                bindVm(fragment)
            } else {
                // 未初始化则没办法马上拿到
                adapter.getFragment(position) {
                    bindVm(it)
                }
            }
        }
    }

    private fun bindVm(f: IllustDetailFragment) {
        f.mViewModel.detailState.doOnPropertyChanged(f.lifecycle) { _, _ ->
            val state = f.mViewModel.detailState.get()
            when (state) {
                is LoadState.Succeed -> mToolbar?.menu?.setGroupVisible(0, true)
                is LoadState.Failed -> mToolbar?.menu?.setGroupVisible(0, false)
            }
        }
        mViewModel = f.mViewModel
        mBinding.vm = mViewModel
    }

    override fun onDestroyView() {
        if (!diffOrientation) {
            if (key != -1) {
                IllustRepository.instance - key
            }
        }
        super.onDestroyView()
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
                .titleBarMarginTop(mBinding.toolbarWrapper)
                .statusBarDarkFont(true)
                .transparentStatusBar()
                .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                .keyboardEnable(true)
                .setOnKeyboardListener(keyboardListener)
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