package com.lyj.fakepixiv.module.novel.series

import android.databinding.Bindable
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import com.lyj.fakepixiv.BR
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.base.IModel
import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.data.model.response.NovelSeriesDetail
import com.lyj.fakepixiv.app.data.source.remote.IllustRepository
import com.lyj.fakepixiv.app.data.source.remote.UserRepository
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.app.utils.Router
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc
 */
class NovelSeriesViewModel : BaseViewModel<IModel?>() {

    override var mModel: IModel? = null

    var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var loadMoreState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var followState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var seriesId: String = ""

    @get:Bindable
    var captionLines = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.captionLines)
        }

    @get:Bindable
    var detail: NovelSeriesDetail = NovelSeriesDetail()
        set(value) {
            field = value
            notifyPropertyChanged(BR.detail)
        }

    var first: ObservableField<Illust> = ObservableField()

    var last: ObservableField<Illust> = ObservableField()

    val data: ObservableList<Illust> = ObservableArrayList()

    var nextUrl: String = ""

    fun load() {
        launch(CoroutineExceptionHandler { _, err ->
            loadState.set(LoadState.Failed(err))
        }) {
            loadState.set(LoadState.Loading)
            data.clear()
            val resp = withContext(Dispatchers.IO) {
                IllustRepository.instance.getNovelSeriesDetail(seriesId)
            }
            resp.first.type = IllustCategory.NOVEL
            resp.last.type = IllustCategory.NOVEL
            resp.novels.forEach { it.type = IllustCategory.NOVEL }
            detail = resp.novel_series_detail
            first.set(resp.first)
            last.set(resp.last)
            data.addAll(resp.novels)
            nextUrl = resp.next_url
            loadState.set(LoadState.Succeed)
        }
    }

    fun loadMore() {
        IllustRepository.instance.loadMore(this, nextUrl, loadMoreState) {
            nextUrl = it.next_url
            data.addAll(it.illusts)
        }
    }

    fun goUserDetail() {
        Router.goUserDetail(detail.user)
    }

    fun goLast() {
        Router.goDetail(0, listOf(last.get()) as List<Illust>)
    }

    fun follow() {
        UserRepository.instance
                .follow(detail.user, followState)
    }
}