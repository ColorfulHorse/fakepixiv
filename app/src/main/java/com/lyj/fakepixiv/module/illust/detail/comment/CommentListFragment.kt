package com.lyj.fakepixiv.module.illust.detail.comment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.databinding.library.baseAdapters.BR
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.CommonListFragment
import com.lyj.fakepixiv.app.constant.EXTRA_ID
import com.lyj.fakepixiv.app.data.model.response.Comment
import com.lyj.fakepixiv.app.utils.attachLoadMore
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

    private lateinit var inputBar: InputBar

    companion object {
        const val EXTRA_ACTION = "EXTRA_ACTION"
        const val ACTION_SHOW = 1
        const val ACTION_REPLY = 2
        fun newInstance(commentId: Long = -1, action: Int = ACTION_SHOW) = CommentListFragment().apply {
            arguments = Bundle().apply {
                putLong(EXTRA_ID, commentId)
                putInt(EXTRA_ACTION, action)
            }
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        mToolbar?.title = getString(R.string.comment)
        mViewModel?.let { vm ->
            inputBar = InputBar(mBinding.input, vm.inputViewModel)
            arguments?.let {
                val id = it.getLong(EXTRA_ID, -1)
                val action = it.getInt(EXTRA_ACTION, ACTION_SHOW)
                if (id != -1L) {
                    if (action == ACTION_SHOW) {
                        mViewModel?.loadApplies(id)
                    }else {
                        inputBar.viewModel.source = vm.data.first { data -> data.data.id == id }.data
                        inputBar.viewModel.show()
                    }
                }
            }

            val adapter = CommentListAdapter(vm.data).apply {
                addItemType(Comment.COMMENT, R.layout.item_comment, BR.vm)
                addItemType(Comment.APPLY, R.layout.item_comment, BR.vm)
            }
            with(mBinding) {
                recyclerView.layoutManager = LinearLayoutManager(context)
                adapter.bindToRecyclerView(recyclerView)
                adapter.bindState(vm.loadState, lifecycle = lifecycle, refreshLayout = mBinding.refreshLayout) {
                    vm.reLoad()
                }
                recyclerView.attachLoadMore(vm.loadMoreState, lifecycle = lifecycle) {
                    vm.loadMore()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        inputBar.viewModel.reset()
    }

    override fun onBackPressedSupport(): Boolean {
        if (inputBar.viewModel.state != InputViewModel.State.CLOSE) {
            inputBar.viewModel.hide()
            return true
        }
        return super.onBackPressedSupport()
    }


    override fun bindLayout(): Int = R.layout.fragment_comment_list

}