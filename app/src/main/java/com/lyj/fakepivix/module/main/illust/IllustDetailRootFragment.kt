package com.lyj.fakepivix.module.main.illust

import android.os.Bundle
import android.support.v7.widget.RecyclerView
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
        mBinding.viewPager.adapter = IllustPagerAdapter(IllustRepository.instance.illustList, position, childFragmentManager)
        mBinding.viewPager.offscreenPageLimit = 2
    }

    override fun bindLayout(): Int = R.layout.fragment_illust_detail_root
}