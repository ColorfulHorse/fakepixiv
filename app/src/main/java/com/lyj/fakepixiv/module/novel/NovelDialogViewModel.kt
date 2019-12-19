package com.lyj.fakepixiv.module.novel

import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.lyj.fakepixiv.app.base.BaseViewModel

import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.data.source.remote.IllustRepository

/**
 * @author greensun
 *
 * @date 2019/8/9
 *
 * @desc
 */
class NovelDialogViewModel(val key: Int, val position: Int) : BaseViewModel() {


    @get:Bindable
    var data: Illust = IllustRepository.instance[key][position]
    set(value) {
        field = value
        notifyPropertyChanged(BR.data)
    }

    init {

    }
}