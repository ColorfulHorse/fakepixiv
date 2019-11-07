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
import com.lyj.fakepixiv.app.utils.bindState
import com.lyj.fakepixiv.databinding.ItemCommentBinding
import com.lyj.fakepixiv.databinding.LayoutFooterCommentBinding
import com.lyj.fakepixiv.module.illust.detail.comment.CommentListAdapter

/**
 * @author greensun
 *
 * @date 2019/6/3
 *
 * @desc 用户简介
 */
class CommentFooter(val context: Context, val viewModel: CommentListViewModel, var mBinding: LayoutFooterCommentBinding? = null): DetailItem {
    override var type: Int = DetailItem.LAYOUT_COMMENT

    val rootView: View by lazy { LayoutInflater.from(context).inflate(R.layout.layout_footer_comment, null) }


    val mAdapter = CommentListAdapter(viewModel.piece).apply {
        addItemType(Comment.COMMENT, R.layout.item_comment, BR.vm)
        addItemType(Comment.APPLY, R.layout.item_comment, BR.vm)
    }

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