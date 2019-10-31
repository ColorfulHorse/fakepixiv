package com.lyj.fakepixiv.module.illust.detail.items

import android.databinding.ObservableField
import com.lyj.fakepixiv.app.base.BaseViewModel

import com.lyj.fakepixiv.module.illust.detail.IllustDetailViewModel

/**
 * @author greensun
 *
 * @date 2019/6/3
 *
 * @desc 详情页用户item
 */
class RelatedCaptionViewModel(val parent: IllustDetailViewModel) : BaseViewModel() {

    var loading = ObservableField<Boolean>(false)
}