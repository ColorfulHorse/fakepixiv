package com.lyj.fakepixiv.module.main.home.illust

import android.databinding.ObservableArrayList
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.base.IModel
import com.lyj.fakepixiv.app.data.model.response.Illust

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc
 */
class RankViewModel : BaseViewModel<IModel?>() {
    override val mModel: IModel? = null

    val data = ObservableArrayList<Illust>()

    fun onData(list: List<Illust>) {
        data.clear()
        data.addAll(list)
    }
}