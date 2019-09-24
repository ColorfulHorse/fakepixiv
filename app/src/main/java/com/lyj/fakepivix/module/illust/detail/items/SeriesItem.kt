package com.lyj.fakepivix.module.illust.detail.items

import android.content.Context
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import com.lyj.fakepivix.R
import com.lyj.fakepivix.databinding.DetailIllustSeriesBinding

/**
 * @author greensun
 *
 * @date 2019/6/3
 *
 * @desc 漫画系列view
 */
class SeriesItem(val context: Context, val viewModel: SeriesItemViewModel): DetailItem {

    override var type: Int = DetailItem.LAYOUT_SERIES

    val mBinding: DetailIllustSeriesBinding by lazy {
        DataBindingUtil.inflate<DetailIllustSeriesBinding>(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                R.layout.detail_illust_series, null, false)
    }



    init {
        mBinding.vm = viewModel
    }
}