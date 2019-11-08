package com.lyj.fakepixiv.module.illust.detail.items

import androidx.databinding.Bindable
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.lyj.fakepixiv.BR
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.data.model.response.Comment
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.data.source.remote.IllustRepository
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.module.illust.detail.comment.CommentViewModel
import io.reactivex.rxkotlin.subscribeBy

/**
 * @author greensun
 *
 * @date 2019/6/3
 *
 * @desc 详情页用户评论
 */
class CommentListViewModel : BaseViewModel() {

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
        val disposable = IllustRepository.instance
                .loadIllustComment(illust.id.toString())
                .doOnSubscribe { loadState.set(LoadState.Loading) }
                .subscribeBy(onNext = { resp ->
                    loadState.set(LoadState.Succeed)
                    nextUrl = resp.next_url
                    if (resp.comments.isEmpty()) {
                        noneData.set(true)
                    }else{
                        showMore.set(resp.comments.size > 2)
                        data.clear()
                        data.addAll(resp.comments.map { CommentViewModel(it) })
                        piece.clear()
                        piece.addAll(data.take(2))
                    }
                }, onError = {
                    loadState.set(LoadState.Failed(it))
                })
        addDisposable(disposable)
    }

    fun loadMore() {

    }
}