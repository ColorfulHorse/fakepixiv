package com.lyj.fakepixiv.module.illust.series

import android.databinding.Bindable
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import com.lyj.fakepixiv.BR
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.base.IModel
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.data.model.response.SeriesDetail
import com.lyj.fakepixiv.app.data.source.remote.IllustRepository
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
class ComicSeriesViewModel : BaseViewModel<IModel?>() {

    override var mModel: IModel? = null

    var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var loadMoreState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var seriesId: String = ""

    @get:Bindable
    var detail: SeriesDetail = SeriesDetail()
    set(value) {
        field = value
        notifyPropertyChanged(BR.detail)
    }

    var first: ObservableField<Illust> = ObservableField()

    val data: ObservableList<Illust> = ObservableArrayList()

    var nextUrl: String = ""

    fun load() {
        launch(CoroutineExceptionHandler { _, err ->
            loadState.set(LoadState.Failed(err))
        }) {
            loadState.set(LoadState.Loading)
            data.clear()
            val resp = withContext(Dispatchers.IO) {
                IllustRepository.instance.getSeriesDetail(seriesId)
            }
            detail = resp.detail
            first.set(resp.first)
            data.addAll(resp.illusts)
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

    fun goFirst() {
        Router.goDetail(0, listOf(first.get()) as List<Illust>)
    }
}