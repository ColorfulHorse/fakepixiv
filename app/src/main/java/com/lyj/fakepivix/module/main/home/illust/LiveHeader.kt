package com.lyj.fakepivix.module.main.home.illust

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.SnapHelper
import android.view.LayoutInflater
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingAdapter
import com.lyj.fakepivix.app.data.model.response.Live
import com.lyj.fakepivix.app.utils.dp2px
import com.lyj.fakepivix.databinding.HeaderLiveBinding
import com.lyj.fakepivix.databinding.ItemHomeLiveBinding
import com.lyj.fakepivix.widget.CommonItemDecoration

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc
 */
class LiveHeader(val context: Context?, val viewModel: LiveViewModel) {

    private val rootView = LayoutInflater.from(context).inflate(R.layout.header_live, null)

    val mBinding: HeaderLiveBinding? = DataBindingUtil.bind(rootView)

    val adapter: BaseBindingAdapter<Live, ItemHomeLiveBinding> = BaseBindingAdapter(R.layout.item_home_live, viewModel.data, BR.data)

    init {
        if (mBinding != null) {
            with(mBinding) {
                vm = viewModel
                val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                recyclerView.layoutManager = layoutManager
                recyclerView.addItemDecoration(CommonItemDecoration.Builder()
                        .draw(false)
                        .edge(16.dp2px(), 5.dp2px())
                        .horizontalWidth(10.dp2px())
                        .build())
                PagerSnapHelper().attachToRecyclerView(recyclerView)
                recyclerView.adapter = adapter
            }
        }
    }
}