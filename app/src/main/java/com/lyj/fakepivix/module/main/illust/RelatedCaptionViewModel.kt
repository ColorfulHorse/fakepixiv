package com.lyj.fakepivix.module.main.illust

import android.databinding.ObservableField
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.module.common.DetailViewModel

/**
 * @author greensun
 *
 * @date 2019/6/3
 *
 * @desc 详情页用户item
 */
class RelatedCaptionViewModel(val parent: IllustDetailViewModel) : BaseViewModel<IModel?>() {
    override val mModel: IModel? = null
    var loading = ObservableField<Boolean>(false)
}