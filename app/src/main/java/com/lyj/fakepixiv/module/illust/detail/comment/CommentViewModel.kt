package com.lyj.fakepixiv.module.illust.detail.comment

import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleOwner
import com.afollestad.materialdialogs.MaterialDialog
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.data.model.bean.MultiPreloadItem
import com.lyj.fakepixiv.app.data.model.response.Comment
import com.lyj.fakepixiv.app.data.source.remote.IllustExtRepository
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.app.utils.AppManager
import com.lyj.fakepixiv.app.utils.Router
import com.lyj.fakepixiv.app.utils.ToastUtil
import com.lyj.fakepixiv.module.illust.detail.items.CommentListViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author green sun
 *
 * @date 2019/11/5
 *
 * @desc 评论item viewModel
 */
class CommentViewModel(val parent: CommentListViewModel, val data: Comment) : BaseViewModel(), MultiPreloadItem by data {

    // 回复是否已经显示
    var applies_show = ObservableField(false)

    val showState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    fun delete() {
        AppManager.instance.top?.let {
            MaterialDialog(it).show {
                title(R.string.title_delete_comment)
                positiveButton(R.string.confirm) {
                    launch(CoroutineExceptionHandler { _, err ->
                        ToastUtil.showToast(R.string.delete_comment_failed)
                    }) {
                        withContext(Dispatchers.IO) {
                            IllustExtRepository.instance
                                    .service
                                    .deleteComment(data.id)
                        }
                        val position = parent.data.indexOfFirst { vm -> vm.data.id == data.id }
                        parent.data.removeAt(position)
                        parent.piece()
                    }
                }
                negativeButton(R.string.cancel)
            }
        }
    }

    /**
     * 回复评论
     */
    fun reply() {
        if (data.preview) {
            val fragment = CommentListFragment.newInstance(data.id, CommentListFragment.ACTION_REPLY)
            fragment.mViewModel = parent
            Router.getTopFragment()?.start(fragment)
        }else {
            parent.inputViewModel.source = data
            parent.inputViewModel.show()
        }
    }

    /**
     * 显示回复
     */
    fun loadApplies() {
        if (data.preview) {
            val fragment = CommentListFragment.newInstance(data.id)
            fragment.mViewModel = parent
            Router.getTopFragment()?.start(fragment)
        }else {
            // 新添加的评论，已经直接展示了
            if (applies_show.get() == true) {
                return
            }
            launch(CoroutineExceptionHandler { _, err ->
                showState.set(LoadState.Failed(err))
            }) {
                showState.set(LoadState.Loading)
                val resp = withContext(Dispatchers.IO) {
                    IllustExtRepository.instance
                            .service
                            .getApplies(data.id)
                }
                applies_show.set(true)
                parent.showApplies(data, resp.comments)
                showState.set(LoadState.Succeed)
            }
        }
    }

    fun goUser() {
        Router.goUserDetail(data.user)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
    }
}