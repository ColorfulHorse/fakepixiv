package com.lyj.fakepixiv.module.novel.series

import android.content.Context
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.databinding.HeaderNovelSeries

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
            it.caption.post {
                viewModel.captionLines = it.caption.lineCount
            }
            it.readMoreTextView.setOnClickListener { _ ->
                it.caption.maxLines = 999
                it.readMoreTextView.visibility = View.GONE
            }
        }
    }
}
