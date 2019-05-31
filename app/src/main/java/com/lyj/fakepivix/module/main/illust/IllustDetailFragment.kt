package com.lyj.fakepivix.module.main.illust

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.PreloadMultiBindingAdapter
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.databinding.FragmentIllustDetailBinding

/**
 * @author greensun
 *
 * @date 2019/5/30
 *
 * @desc 作品详情
 */
class IllustDetailFragment : FragmentationFragment<FragmentIllustDetailBinding, IllustDetailViewModel>() {

    override var mViewModel: IllustDetailViewModel = IllustDetailViewModel()

    companion object {

        private const val EXTRA_POSITION = "EXTRA_POSITION"
        fun newInstance(position: Int): IllustDetailFragment {
            return IllustDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_POSITION, position)
                }
            }
        }
    }

    private var position = -1

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var mAdapter: IllustDetailAdapter

    override fun init(savedInstanceState: Bundle?) {
        arguments?.let {
            position = it.getInt(EXTRA_POSITION, -1)
            mViewModel.position = position
        }
        layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
        mAdapter = IllustDetailAdapter(mViewModel.data)
        with(mBinding) {
            recyclerView.layoutManager = layoutManager
            mAdapter.bindToRecyclerView(recyclerView)
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)

    }

    override fun immersionBarEnabled(): Boolean {
        return false
    }

    override fun bindLayout(): Int = R.layout.fragment_illust_detail
}