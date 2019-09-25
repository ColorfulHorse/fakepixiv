package com.lyj.fakepivix.module.illust.series

import android.databinding.Bindable
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.model.response.SeriesDetail
import com.lyj.fakepivix.app.data.model.response.User
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.network.LoadState
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
}