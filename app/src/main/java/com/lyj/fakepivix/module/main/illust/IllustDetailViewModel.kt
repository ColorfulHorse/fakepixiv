package com.lyj.fakepivix.module.main.illust

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.model.response.ImageUrls
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy

/**
 * @author greensun
 *
 * @date 2019/5/30
 *
 * @desc
 */
class IllustDetailViewModel : BaseViewModel<IModel?>() {

    override val mModel: IModel? = null

    var illust: Illust = Illust()

    var data: ObservableList<Illust> = ObservableArrayList()

    var position = -1
        set(value) {
            field = value
            val illust = IllustRepository.instance.illustList[position]
            this.illust = illust
            if (illust.meta_pages.isNotEmpty()) {
                val list = illust.meta_pages.map {
                    Illust(image_urls = it.image_urls)
                }
                data.addAll(list)
            }else {
                data.add(Illust(image_urls = ImageUrls(illust.meta_single_page.original_image_url)))
            }
        }

    fun load() {
        val disposable = IllustRepository.instance
                .loadRelatedIllust(illust.id.toString())
                .subscribeBy(onNext = {
                    data.addAll(it.illusts)
                }, onError = {

                })
        addDisposable(disposable)
    }

}