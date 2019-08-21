package com.lyj.fakepivix.module.main.illust

import android.databinding.Bindable
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.model.response.ImageUrls
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.data.source.remote.UserRepository
import com.lyj.fakepivix.app.databinding.onPropertyChangedCallback
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.module.common.DetailViewModel
import io.reactivex.rxkotlin.subscribeBy

/**
 * @author greensun
 *
 * @date 2019/5/30
 *
 * @desc 插画漫画详情
 */
class IllustDetailViewModel : DetailViewModel() {

    var data: ObservableList<Illust> = ObservableArrayList()

    val relatedCaptionFooterViewModel = RelatedCaptionViewModel(this)

    init {
        this + relatedCaptionFooterViewModel
    }


    override fun setData(key: Int, position: Int) {
        super.setData(key, position)
        if (illust.meta_pages.isNotEmpty()) {
            val first = illust.copy(type = Illust.META)
            val list = illust.meta_pages.map {
                Illust(image_urls = it.image_urls, type = Illust.META)
            }.toMutableList()
            list[0] = first
            data.addAll(list)
        } else {
            //data.add(Illust(image_urls = ImageUrls(original = illust.meta_single_page.original_image_url), type = Illust.META))
            data.add(illust.copy(type = Illust.META).apply {
                image_urls.original = meta_single_page.original_image_url
            })
        }
        if (data.size > 1) {
            captionVisibility.set(true)
        }
        total.set(data.size)
    }


    fun load() {
        val disposable = IllustRepository.instance
                .loadRelatedIllust(illust.id.toString())
                .doOnSubscribe { loadState.set(LoadState.Loading) }
                .subscribeBy(onNext = {
                    loadState.set(LoadState.Succeed)
                    //data.clear()
                    data.addAll(it.illusts)
                }, onError = {
                    loadState.set(LoadState.Failed(it))
                })
        addDisposable(disposable)
    }

}