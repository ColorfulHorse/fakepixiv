package com.lyj.fakepivix.module.main.novel

import android.databinding.Bindable
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.source.remote.IllustRepository

/**
 * @author greensun
 *
 * @date 2019/8/9
 *
 * @desc
 */
class NovelDialogViewModel(val key: Int, val position: Int) : BaseViewModel<IModel?>() {
    override val mModel: IModel? = null

    @get:Bindable
    var data: Illust = IllustRepository.instance[key][position]
    set(value) {
        field = value
        notifyPropertyChanged(BR.data)
    }

    init {

    }
}