package com.lyj.fakepivix.module.main.home.illust

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.view.LayoutInflater
import android.view.View
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingAdapter
import com.lyj.fakepivix.app.data.model.response.Live
import com.lyj.fakepivix.app.databinding.OnPropertyChangedCallbackImp
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.app.utils.dp2px
import com.lyj.fakepivix.databinding.HeaderLiveBinding
import com.lyj.fakepivix.databinding.ItemHomeLiveBinding
import com.lyj.fakepivix.widget.CommonItemDecoration
import kotlinx.android.synthetic.main.layout_error_small.view.*

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

    private val loadingView: View by lazy { LayoutInflater.from(context).inflate(R.layout.layout_common_loading, null) }


    init {
        if (mBinding != null) {
            with(mBinding) {
                errorView.reload.setOnClickListener {
                    viewModel.load()
                }
                adapter.emptyView = loadingView
                val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                recyclerView.layoutManager = layoutManager
                recyclerView.addItemDecoration(CommonItemDecoration.Builder()
                        .edge(16.dp2px(), 5.dp2px())
                        .dividerWidth(10.dp2px(), 10.dp2px())
                        .build())
                PagerSnapHelper().attachToRecyclerView(recyclerView)
                recyclerView.adapter = adapter

                with(viewModel) {
                    loadState.addOnPropertyChangedCallback(OnPropertyChangedCallbackImp { _, _ ->
                        loadFailed = when (loadState.get()) {
                            is LoadState.Failed -> {
                                true
                            }
                            else -> {
                                false
                            }
                        }
                    })
                }
            }
        }
    }
}