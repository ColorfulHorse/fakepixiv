package com.lyj.fakepivix.module.main.illust

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.source.remote.IllustRepository

/**
 * @author greensun
 *
 * @date 2019/5/30
 *
 * @desc
 */
class IllustDetailViewModel : BaseViewModel<IModel?>() {

    override val mModel: IModel? = null

    var illust: ObservableField<Illust> = ObservableField(Illust())

    var data: ObservableList<Illust> = ObservableArrayList()

    var position = -1
        set(value) {
            field = value
            val illust = IllustRepository.instance.illustList[position]
            data.addAll(illust.meta_pages.map {
                Illust(image_urls = it.image_urls)
            })
        }


}