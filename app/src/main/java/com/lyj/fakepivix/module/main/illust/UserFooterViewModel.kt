package com.lyj.fakepivix.module.main.illust

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.model.response.User

/**
 * @author greensun
 *
 * @date 2019/6/3
 *
 * @desc
 */
class UserFooterViewModel() : BaseViewModel<IModel?>() {
    override val mModel: IModel? = null

    var user = ObservableField<User>()
    var data = ObservableArrayList<Illust>()

    fun load() {

    }
}