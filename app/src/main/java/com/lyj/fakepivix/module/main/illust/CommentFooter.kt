package com.lyj.fakepivix.module.main.illust

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingAdapter
import com.lyj.fakepivix.app.data.model.response.Comment
import com.lyj.fakepivix.app.databinding.onPropertyChangedCallback
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.databinding.*
import kotlinx.android.synthetic.main.layout_error_small.view.*

/**
 * @author greensun
 *
 * @date 2019/6/3
 *
 * @desc 用户简介
 */
class CommentFooter(val context: Context, val viewModel: CommentFooterViewModel, var mBinding: LayoutFooterCommentBinding? = null) {

    val rootView: View by lazy { LayoutInflater.from(context).inflate(R.layout.layout_footer_comment, null) }

    private val loadingView: View by lazy { LayoutInflater.from(context).inflate(R.layout.layout_common_loading_white, null) }

    private val errorView: View by lazy { LayoutInflater.from(context).inflate(R.layout.layout_error_small, null) }

    var mAdapter = BaseBindingAdapter<Comment, ItemCommentBinding>(R.layout.item_comment, viewModel.data, BR.data)

    init {
        if (mBinding == null) {
            mBinding = DataBindingUtil.bind(rootView)
        }
        mBinding?.vm = viewModel
        mAdapter.emptyView = loadingView
//
        mBinding?.let {
            mAdapter.bindToRecyclerView(it.recyclerView)
            it.recyclerView.layoutManager = LinearLayoutManager(context)
        }

        errorView.reload.setOnClickListener {
            viewModel.reLoad()
        }
        viewModel.loadState.addOnPropertyChangedCallback(onPropertyChangedCallback { _, _ ->
            when(viewModel.loadState.get()) {
                is LoadState.Loading -> {
                    mAdapter.emptyView = loadingView
                }
                is LoadState.Failed -> {
                    mAdapter.emptyView = errorView
                }
                is LoadState.Succeed -> {

                }
            }
        })
    }
}