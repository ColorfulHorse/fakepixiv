package com.lyj.fakepivix.module.illust.detail

import android.content.Context
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import com.lyj.fakepivix.R
import com.lyj.fakepivix.databinding.DetailIllustSeriesBinding
import com.lyj.fakepivix.databinding.FooterRelatedCaptionBinding
import kotlinx.android.synthetic.main.layout_error_small.view.*

/**
 * @author greensun
 *
 * @date 2019/6/3
 *
 * @desc 漫画系列view
 */
class SeriesItem(val context: Context, val viewModel: SeriesItemViewModel) {

    val rootView: View by lazy { LayoutInflater.from(context).inflate(R.layout.detail_illust_series, null) }

    val mBinding: DetailIllustSeriesBinding?


    init {
        mBinding = DataBindingUtil.bind(rootView)

        mBinding?.let {

        }

//        viewModel.parent.loadState.addOnPropertyChangedCallback(onPropertyChangedCallback { _, _ ->
//            when(viewModel.parent.loadState.get()) {
//                is LoadState.Loading -> {
//                    mAdapter.emptyView = loadingView
//                }
//                is LoadState.Failed -> {
//                    mAdapter.emptyView = errorView
//                }
//            }
//        })
    }
}