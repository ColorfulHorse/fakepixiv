package com.lyj.fakepivix.module.novel.series

import android.databinding.Bindable
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import android.widget.TextView
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.app.App
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.model.response.NovelSeriesDetail
import com.lyj.fakepivix.app.data.model.response.SeriesDetail
import com.lyj.fakepivix.app.data.model.response.User
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.data.source.remote.UserRepository
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.app.utils.Router
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