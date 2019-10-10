package com.lyj.fakepivix.module.illust.detail

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.module.common.DetailViewModel
import com.lyj.fakepivix.module.illust.detail.items.RelatedCaptionViewModel
import com.lyj.fakepivix.module.illust.detail.items.SeriesItemViewModel
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author greensun
 *
 * @date 2019/5/30
 *
 * @desc 插画漫画详情vm
 */
class IllustDetailViewModel : DetailViewModel() {

    var data: ObservableList<Illust> = ObservableArrayList()

    var detailState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var illustId = -1L

    override val seriesItemViewModel = SeriesItemViewModel(this)
    val relatedCaptionFooterViewModel = RelatedCaptionViewModel(this)

    init {
        this + seriesItemViewModel + relatedCaptionFooterViewModel
    }


    override fun setData(data: Illust) {
        super.setData(data)
        if (illust.meta_pages.isNotEmpty()) {
            val first = illust.copy(type = Illust.META)
            val list = illust.meta_pages.map {
                Illust(image_urls = it.image_urls, type = Illust.META)
            }.toMutableList()
            list[0] = first
            this.data.addAll(list)
        } else {
            this.data.add(illust.copy(type = Illust.META).apply {
                image_urls.original = meta_single_page.original_image_url
            })
        }
        if (this.data.size > 1) {
            captionVisibility.set(true)
        }
        total.set(this.data.size)
    }

    /**
     * 加载相关作品
     */
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

    /**
     * 加载作品信息
     */
    fun loadDetail() {
        if (illustId != -1L) {
            launch(CoroutineExceptionHandler { _, err ->
                detailState.set(LoadState.Failed(err))
            }) {
                detailState.set(LoadState.Loading)
                val res = withContext(Dispatchers.IO) {
                    IllustRepository.instance
                            .getIllustDetail(illustId.toString())
                }

                setData(res.illust)
                detailState.set(LoadState.Succeed)
            }
        }
    }

}