package com.lyj.fakepixiv.module.illust.detail

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import androidx.lifecycle.LifecycleOwner
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.data.source.remote.IllustRepository
import com.lyj.fakepixiv.app.data.source.remote.checkVisible
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.module.common.DetailViewModel
import com.lyj.fakepixiv.module.illust.detail.items.RelatedCaptionViewModel
import com.lyj.fakepixiv.module.illust.detail.items.SeriesItemViewModel
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

    var nextUrl = ""

    override val seriesItemViewModel = SeriesItemViewModel(this)
    val relatedCaptionFooterViewModel = RelatedCaptionViewModel(this)

    val loadMoreState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    init {
        this + seriesItemViewModel + relatedCaptionFooterViewModel
    }

    /**
     * 添加作品内多张图片为item
     */
    override fun setData(data: Illust) {
        super.setData(data)
        if (illust.meta_pages.isNotEmpty()) {
            //val first = illust.copy(type = Illust.META)
            val list = illust.meta_pages.map {
                data.copy(image_urls = it.image_urls, type = Illust.META)
            }.toMutableList()
            //list[0] = first
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
                    //data.clear()
                    data.addAll(it.illusts)
                    nextUrl = it.next_url
                    loadState.set(LoadState.Succeed)
                }, onError = {
                    loadState.set(LoadState.Failed(it))
                })
        addDisposable(disposable)
    }

    fun loadMore() {
        if (nextUrl.isBlank())
            return
        val disposable = IllustRepository.instance
                .loadMore(nextUrl)
                .doOnSubscribe {
                    loadMoreState.set(LoadState.Loading)
                }
                .subscribeBy(onNext = {
                    nextUrl = it.next_url
                    data.addAll(it.illusts)
                    loadMoreState.set(LoadState.Succeed)
                }, onError = {
                    loadMoreState.set(LoadState.Failed(it))
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
                res.illust.checkVisible()
                setData(res.illust)
                detailState.set(LoadState.Succeed)
            }
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        if (owner is AboutDialogFragment) {
            return
        }
        super.onDestroy(owner)
    }

}