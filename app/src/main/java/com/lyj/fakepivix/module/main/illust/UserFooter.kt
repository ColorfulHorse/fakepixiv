package com.lyj.fakepivix.module.main.illust

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingAdapter
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.databinding.OnPropertyChangedCallbackImp
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.app.utils.dp2px
import com.lyj.fakepivix.databinding.ItemIllustBinding
import com.lyj.fakepivix.databinding.LayoutFooterUserBinding
import com.lyj.fakepivix.widget.CommonItemDecoration
import kotlinx.android.synthetic.main.layout_error_small.view.*

/**
 * @author greensun
 *
 * @date 2019/6/3
 *
 * @desc 用户简介
 */
class UserFooter(val context: Context, val viewModel: UserFooterViewModel, var mBinding: LayoutFooterUserBinding? = null) {

    val rootView: View by lazy { LayoutInflater.from(context).inflate(R.layout.layout_footer_user, null) }

    private val loadingView: View by lazy { LayoutInflater.from(context).inflate(R.layout.layout_common_loading_white, null) }

    private val errorView: View by lazy { LayoutInflater.from(context).inflate(R.layout.layout_error_small, null) }


    var mAdapter = BaseBindingAdapter<Illust, ItemIllustBinding>(R.layout.item_illust, viewModel.data, BR.illust)

    init {
        if (mBinding == null) {
            mBinding = DataBindingUtil.bind(rootView)
        }
        mBinding?.let {
            it.vm = viewModel
            it.recyclerView.layoutManager = GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
            it.recyclerView.addItemDecoration(CommonItemDecoration.Builder().dividerWidth(1.dp2px(), 0).draw(false).build())
            mAdapter.bindToRecyclerView(it.recyclerView)
        }
        errorView.reload.setOnClickListener {
            viewModel.reLoad()
        }
        viewModel.loadState.addOnPropertyChangedCallback(OnPropertyChangedCallbackImp { _, _ ->
            when(viewModel.loadState.get()) {
                is LoadState.Loading -> {
                    mAdapter.emptyView = loadingView
                }
                is LoadState.Failed -> {
                    mAdapter.emptyView = errorView
                }
            }
        })
    }
}