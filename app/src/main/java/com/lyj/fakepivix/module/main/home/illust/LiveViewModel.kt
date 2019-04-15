package com.lyj.fakepivix.module.main.home.illust

import android.databinding.ObservableArrayList
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.data.model.response.Live

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc
 */
class LiveViewModel : BaseViewModel<IModel?>() {
    override val mModel: IModel? = null

    val data = ObservableArrayList<Live>()

    fun load() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}