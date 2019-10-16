package com.lyj.fakepixiv.module.illust.detail.items

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import com.lyj.fakepixiv.BR
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.adapter.BaseBindingAdapter
import com.lyj.fakepixiv.app.data.model.response.Comment
import com.lyj.fakepixiv.app.databinding.onPropertyChangedCallback
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.app.utils.bindState
import com.lyj.fakepixiv.databinding.ItemCommentBinding
import com.lyj.fakepixiv.databinding.LayoutFooterCommentBinding
import kotlinx.android.synthetic.main.layout_error_small.view.*

/**
 * @author greensun
 *
 * @date 2019/6/3
 *
 * @desc 用户简介
 */
class CommentFooter(val context: Context, val viewModel: CommentFooterViewModel, var mBinding: LayoutFooterCommentBinding? = null): DetailItem {
    override var type: Int = DetailItem.LAYOUT_COMMENT

    val rootView: View by lazy { LayoutInflater.from(context).inflate(R.layout.layout_footer_comment, null) }


    var mAdapter = BaseBindingAdapter<Comment, ItemCommentBinding>(R.layout.item_comment, viewModel.data, BR.data)

    init {
        if (mBinding == null) {
            mBinding = DataBindingUtil.bind(rootView)
        }
        mBinding?.vm = viewModel

        mBinding?.let {
            mAdapter.bindToRecyclerView(it.recyclerView)
            it.recyclerView.layoutManager = LinearLayoutManager(context)
            mAdapter.bindState(viewModel.loadState, errorRes = R.layout.layout_error_small) {
                viewModel.load()
            }
        }
    }
}