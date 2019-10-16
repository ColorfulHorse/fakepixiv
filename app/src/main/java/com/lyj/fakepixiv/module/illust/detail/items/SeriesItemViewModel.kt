package com.lyj.fakepixiv.module.illust.detail.items

import android.databinding.Bindable
import android.databinding.ObservableField
import com.lyj.fakepixiv.BR
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.base.IModel
import com.lyj.fakepixiv.app.data.model.response.SeriesContext
import com.lyj.fakepixiv.app.data.source.remote.IllustRepository
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.app.utils.Router
import com.lyj.fakepixiv.module.illust.detail.IllustDetailViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author greensun
 *
 * @date 2019/6/3
 *
 * @desc 漫画系列item
 */
class SeriesItemViewModel(val parent: IllustDetailViewModel) : BaseViewModel<IModel?>() {
    override val mModel: IModel? = null

    var loadState = ObservableField<LoadState>(LoadState.Idle)

    @get: Bindable
    var data = SeriesContext()
        set(value) {
            field = value
            notifyPropertyChanged(BR.data)
        }

    fun load() {
        launch(CoroutineExceptionHandler { _, err ->
            loadState.set(LoadState.Failed(err))
        }) {
            loadState.set(LoadState.Loading)
            val resp = withContext(Dispatchers.IO) {
                IllustRepository.instance
                        .getSeriesContext(parent.illust.id.toString())
            }
            data = resp.context
            loadState.set(LoadState.Succeed)
        }
    }

    fun goNext() {
        data.next?.let {
            Router.goDetail(0, listOf(it))
        }
    }

    fun goPrev() {
        data.prev?.let {
            Router.goDetail(0, listOf(it))
        }
    }

    fun goSeries() {
        parent.illust.series?.let {
            Router.goIllustSeries(it.id.toString())
        }
    }
}