package com.lyj.fakepixiv.module.illust.series

import android.content.Context
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.databinding.HeaderIllustSeriesBinding

/**
 * @author greensun
 * @date 2019/9/26
 * @desc
 */
class SeriesListHeader(val context: Context, val viewModel: ComicSeriesViewModel) {

    val rootView: View by lazy { LayoutInflater.from(context).inflate(R.layout.header_illust_series, null) }

    var mBinding: HeaderIllustSeriesBinding? = null

    init {
        if (mBinding == null) {
            mBinding = DataBindingUtil.bind(rootView)
        }

        mBinding?.let {
            it.vm = viewModel
        }
    }
}
