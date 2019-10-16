package com.lyj.fakepixiv.module.illust.detail.items

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.base.IModel
import com.lyj.fakepixiv.app.data.model.response.Comment
import com.lyj.fakepixiv.app.data.source.remote.IllustRepository
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.module.common.DetailViewModel
import io.reactivex.rxkotlin.subscribeBy

/**
 * @author greensun
 *
 * @date 2019/6/3
 *
 * @desc 详情页用户评论
 */
class CommentFooterViewModel(val parent: DetailViewModel) : BaseViewModel<IModel?>() {
    override val mModel: IModel? = null

    //var illust = ObservableField<Illust>()

    var data = ObservableArrayList<Comment>()
    var noneData = ObservableField(false)
    var showMore = ObservableField(false)

    var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var nextUrl = ""

    /**
     * 由于共用了viewModel，需要限制初次加载
     */
    fun load() {
        if (loadState.get() is LoadState.Idle) {
            reLoad()
        }
    }

    fun reLoad() {
        val illust = parent.illust
        val disposable = IllustRepository.instance
                .loadIllustComment(illust.id.toString())
                .doOnSubscribe { loadState.set(LoadState.Loading) }
                .subscribeBy(onNext = {
                    loadState.set(LoadState.Succeed)
                    nextUrl = it.next_url
                    if (it.comments.isEmpty()) {
                        noneData.set(true)
                    }else{
                        showMore.set(it.comments.size > 2)
                        data.clear()
                        data.addAll(it.comments.take(2))
                    }
                }, onError = {
                    loadState.set(LoadState.Failed(it))
                })
        addDisposable(disposable)
    }
}