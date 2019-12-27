package com.lyj.fakepixiv.module.illust.detail.items

import android.content.Context
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.application.ApplicationLike.Companion.context
import com.lyj.fakepixiv.databinding.DetailIllustSeriesBinding
import org.jetbrains.annotations.NotNull

/**
 * @author greensun
 *
 * @date 2019/6/3
 *
 * @desc 漫画系列view
 */
class SeriesItem(val context: Context,
                 val viewModel: SeriesItemViewModel,
                 var binding: DetailIllustSeriesBinding? = null): DetailItem {

    override var type: Int = DetailItem.LAYOUT_SERIES

    val rootView: View by lazy { LayoutInflater.from(context).inflate(R.layout.detail_illust_series, null) }


    init {
        if (binding == null) {
            binding = DataBindingUtil.bind(rootView)
            binding?.vm = viewModel
        }
    }
}