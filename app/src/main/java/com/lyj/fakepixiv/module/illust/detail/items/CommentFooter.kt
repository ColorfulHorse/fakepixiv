package com.lyj.fakepixiv.module.illust.detail.items

import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.library.baseAdapters.BR
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.adapter.BaseBindingAdapter
import com.lyj.fakepixiv.app.data.model.response.Comment
import com.lyj.fakepixiv.app.utils.Router
import com.lyj.fakepixiv.app.utils.bindState
import com.lyj.fakepixiv.databinding.ItemCommentBinding
import com.lyj.fakepixiv.databinding.LayoutFooterCommentBinding
import com.lyj.fakepixiv.module.illust.detail.comment.CommentListAdapter
import com.lyj.fakepixiv.module.illust.detail.comment.CommentListFragment

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
            mAdapter.bindState(viewModel.loadState, loadingRes = R.layout.layout_common_loading_white, errorRes = R.layout.layout_error_small) {
                viewModel.load()
            }
            it.more.setOnClickListener {
                val fragment = CommentListFragment.newInstance()
                fragment.mViewModel = viewModel
                Router.getTopFragment()?.start(fragment)
            }
        }
    }
}