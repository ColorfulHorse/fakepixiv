package com.lyj.fakepivix.module.main.novel

import android.arch.lifecycle.LifecycleObserver
import android.os.Bundle
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.databinding.FragmentIllustDetailBinding
import com.lyj.fakepivix.module.main.illust.IllustDetailFragment
import com.lyj.fakepivix.module.main.illust.IllustDetailViewModel

/**
 * @author greensun
 *
 * @date 2019/8/12
 *
 * @desc
 */
class NovelDetailFragment : FragmentationFragment<FragmentIllustDetailBinding, IllustDetailViewModel>() {

    override lateinit var mViewModel: IllustDetailViewModel

    companion object {
        private const val EXTRA_POSITION = "EXTRA_POSITION"
        private const val EXTRA_KEY = "EXTRA_KEY"
        fun newInstance(position: Int, key: Int): IllustDetailFragment {
            return IllustDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_POSITION, position)
                    putInt(EXTRA_KEY, key)
                }
            }
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        arguments?.let {
            val position = it.getInt(EXTRA_POSITION, -1)
            val key = it.getInt(EXTRA_KEY, -1)
            mViewModel = IllustDetailViewModel(key, position)
            mViewModel.captionVisibility.set(true)
            lifecycle.addObserver(mViewModel)
            mBinding.setVariable(bindViewModel(), mViewModel)
        }

    }

    override fun bindLayout(): Int = R.layout.fragment_novel_detail
}