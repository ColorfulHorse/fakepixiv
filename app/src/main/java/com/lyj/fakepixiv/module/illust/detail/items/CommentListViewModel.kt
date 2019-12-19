package com.lyj.fakepixiv.module.illust.detail.items

import androidx.databinding.Bindable
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.library.baseAdapters.BR
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.data.model.response.Comment
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.data.source.remote.IllustExtRepository
import com.lyj.fakepixiv.app.data.source.remote.IllustRepository
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.module.illust.detail.comment.CommentViewModel
import com.lyj.fakepixiv.module.illust.detail.comment.InputViewModel
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author greensun
 *
 * @date 2019/6/3
 *
 * @desc 详情页用户评论
 */
class CommentListViewModel : BaseViewModel() {

    val inputViewModel = InputViewModel(this)

    @get:Bindable
    var illust = Illust()
        set(value) {
            field = value
            notifyPropertyChanged(BR.illust)
        }

    // 展示部分
    val piece = ObservableArrayList<CommentViewModel>()

    val data = ObservableArrayList<CommentViewModel>()

    // 空数据
    var noneData = ObservableField(false)
    // 两条以上
    var showMore = ObservableField(false)

    var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var loadMoreState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var nextUrl = ""

    /**
     * 由于共用了viewModel，需要限制初次加载
     */
    fun load() {
        if (loadState.get() !is LoadState.Succeed && data.isEmpty()) {
            reLoad()
        }
    }

    fun reLoad() {
        val disposable = IllustExtRepository.instance
                .loadIllustComment(illust.id.toString())
                .doOnSubscribe { loadState.set(LoadState.Loading) }
                .subscribeBy(onNext = { resp ->
                    loadState.set(LoadState.Succeed)
                    nextUrl = resp.next_url
                    if (resp.comments.isEmpty()) {
                        noneData.set(true)
                    } else {
                        showMore.set(resp.comments.size > 2)
                        data.clear()
                        data.addAll(resp.comments.map { CommentViewModel(this, it) })
                        piece()
                    }
                }, onError = {
                    loadState.set(LoadState.Failed(it))
                })
        addDisposable(disposable)
    }

    /**
     * 展示前两条
     */
    fun piece() {
        piece.clear()
        val list = data.take(2)
        val res = list.map { CommentViewModel(this, it.data.copy(preview = true)) }
        piece.addAll(res)
    }

    fun loadMore() {
        if (nextUrl.isBlank())
            return
        launch(CoroutineExceptionHandler { _, err ->
            loadMoreState.set(LoadState.Failed(err))
        }) {
            loadMoreState.set(LoadState.Loading)
            val resp = withContext(Dispatchers.IO) {
                IllustExtRepository.instance
                        .loadMoreComment(nextUrl)
            }
            nextUrl = resp.next_url
            data.addAll(resp.comments.map { CommentViewModel(this@CommentListViewModel, it) })
            loadMoreState.set(LoadState.Succeed)
        }
    }

    /**
     * 显示回复
     */
    fun showApplies(comment: Comment, list: List<Comment>) {
        val position = data.indexOfFirst {
            it.data.id == comment.id
        }
        data.addAll(position + 1, list.map {
            CommentViewModel(this, it.apply {
                type = Comment.APPLY
                parentId = comment.id
            })
        })
    }

    fun loadApplies(id: Long) {
        data.first { it.data.id == id }.loadApplies()
    }
}