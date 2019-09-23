package com.lyj.fakepivix.module.illust.detail

import android.databinding.ObservableField
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.data.model.response.SeriesContext
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.network.LoadState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author greensun
 *
 * @date 2019/6/3
 *
 * @desc 详情页用户item
 */
class SeriesItemViewModel(val parent: IllustDetailViewModel) : BaseViewModel<IModel?>() {
    override val mModel: IModel? = null

    var loadState = ObservableField<LoadState>(LoadState.Idle)

    val data = ObservableField<SeriesContext>()

    fun load() {
        launch(CoroutineExceptionHandler {_, err ->
            loadState.set(LoadState.Failed(err))
        }) {
            loadState.set(LoadState.Loading)
            val resp = withContext(Dispatchers.IO) {
                IllustRepository.instance
                        .getSeriesContext(parent.illust.id.toString())
            }
            data.set(resp.context)
            loadState.set(LoadState.Succeed)
        }
    }
}