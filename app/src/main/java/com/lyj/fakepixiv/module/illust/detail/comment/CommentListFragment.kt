package com.lyj.fakepixiv.module.illust.detail.comment

import android.graphics.ColorSpace
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.lyj.fakepixiv.BR
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.BackFragment
import com.lyj.fakepixiv.app.base.CommonListFragment
import com.lyj.fakepixiv.app.data.model.response.Comment
import com.lyj.fakepixiv.app.databinding.attachLoadMore
import com.lyj.fakepixiv.app.utils.bindState
import com.lyj.fakepixiv.databinding.FragmentCommentListBinding
import com.lyj.fakepixiv.module.illust.detail.items.CommentListViewModel

/**
 * @author greensun
 *
 * @date 2019/3/20
 *
 * @desc 评论列表页
 */
class CommentListFragment : CommonListFragment<FragmentCommentListBinding, CommentListViewModel?>() {

    override var mViewModel: CommentListViewModel? = null

    override fun init(savedInstanceState: Bundle?) {
        mViewModel?.let {vm ->
            val adapter = CommentListAdapter(vm.data).apply {
                addItemType(Comment.COMMENT, R.layout.item_comment, BR.vm)
                addItemType(Comment.APPLY, R.layout.item_comment, BR.vm)
            }
            with(mBinding) {
                recyclerView.layoutManager = LinearLayoutManager(context)
                adapter.bindToRecyclerView(recyclerView)
                adapter.bindState(vm.loadState) {
                    vm.reLoad()
                }
                recyclerView.attachLoadMore(vm.loadMoreState) {
                    vm.loadMore()
                }
            }
        }
    }

    override fun bindLayout(): Int = R.layout.fragment_comment_list

}