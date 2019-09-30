package com.lyj.fakepivix.module.novel.series

import android.content.Context
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import com.lyj.fakepivix.R
import com.lyj.fakepivix.databinding.HeaderNovelSeries

/**
 * @author greensun
 * @date 2019/9/26
 * @desc
 */
class NovelSeriesHeader(val context: Context, val viewModel: NovelSeriesViewModel) {

    val rootView: View by lazy { LayoutInflater.from(context).inflate(R.layout.header_novel_series, null) }

    var mBinding: HeaderNovelSeries? = null

    init {
        if (mBinding == null) {
            mBinding = DataBindingUtil.bind(rootView)
        }

        mBinding?.let {
            it.vm = viewModel
        }
    }
}
