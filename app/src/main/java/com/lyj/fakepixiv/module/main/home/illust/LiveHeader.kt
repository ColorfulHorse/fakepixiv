package com.lyj.fakepixiv.module.main.home.illust

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.view.LayoutInflater
import android.view.View
import com.lyj.fakepixiv.BR
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.adapter.BaseBindingAdapter
import com.lyj.fakepixiv.app.data.model.response.Live
import com.lyj.fakepixiv.app.databinding.onPropertyChangedCallback
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.app.utils.bindState
import com.lyj.fakepixiv.app.utils.dp2px
import com.lyj.fakepixiv.databinding.HeaderLiveBinding
import com.lyj.fakepixiv.databinding.ItemHomeLiveBinding
import com.lyj.fakepixiv.widget.CommonItemDecoration

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc 人气直播
 */
class LiveHeader(val context: Context?, val viewModel: LiveViewModel) {

    private val rootView = LayoutInflater.from(context).inflate(R.layout.header_live, null)

    val mBinding: HeaderLiveBinding? = DataBindingUtil.bind(rootView)

    val adapter: BaseBindingAdapter<Live, ItemHomeLiveBinding> = BaseBindingAdapter(R.layout.item_home_live, viewModel.data, BR.data)

    init {
        if (mBinding != null) {
            with(mBinding) {
                val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                recyclerView.layoutManager = layoutManager
                recyclerView.addItemDecoration(CommonItemDecoration.Builder()
                        .edge(16.dp2px(), 5.dp2px())
                        .dividerWidth(10.dp2px(), 10.dp2px())
                        .build())
                PagerSnapHelper().attachToRecyclerView(recyclerView)
                recyclerView.adapter = adapter
                adapter.bindState(viewModel.loadState, errorRes = R.layout.layout_error_small) {
                    viewModel.load()
                }
            }
        }
    }
}