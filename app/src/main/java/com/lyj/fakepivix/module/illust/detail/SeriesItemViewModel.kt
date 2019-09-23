package com.lyj.fakepivix.module.illust.detail

import android.databinding.ObservableField
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.network.LoadState

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

    fun load() {
//        IllustRepository.instance
//                .getSeriesContext(parent.illust)
    }
}