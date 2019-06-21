package com.lyj.fakepivix.module.main.illust

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.model.response.ImageUrls
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.network.LoadState
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

    var illust = ObservableField<Illust>()

    var data: ObservableList<Illust> = ObservableArrayList()

    var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var total = ObservableField(0)
    var current = ObservableField(1)
    var toolbarVisibility = ObservableField(true)

    val userFooterViewModel = UserFooterViewModel(this)
    val commentFooterViewModel = CommentFooterViewModel(this)
    val relatedCaptionFooterViewModel = RelatedCaptionViewModel(this)

    var position = -1
        set(value) {
            field = value
            initData()
        }

    private fun initData() {
        val illust = IllustRepository.instance.illustList[position]
        this.illust.set(illust)
        if (illust.meta_pages.isNotEmpty()) {
            val list = illust.meta_pages.map {
                Illust(image_urls = it.image_urls)
            }
            data.addAll(list)
        } else {
            data.add(Illust(image_urls = ImageUrls(illust.meta_single_page.original_image_url)))
        }
        total.set(data.size)
        //userFooterViewModel.user.set(illust.user)
        //commentFooterViewModel.illust.set(illust)
    }

    fun load() {
        val disposable = IllustRepository.instance
                .loadRelatedIllust(illust.get()?.id.toString())
                .doOnSubscribe { loadState.set(LoadState.Loading) }
                .subscribeBy(onNext = {
                    loadState.set(LoadState.Succeed)
                    data.clear()
                    data.addAll(it.illusts)
                }, onError = {
                    loadState.set(LoadState.Failed(it))
                })
        addDisposable(disposable)
    }

}