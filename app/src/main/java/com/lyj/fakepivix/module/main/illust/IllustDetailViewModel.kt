package com.lyj.fakepivix.module.main.illust

import android.databinding.Bindable
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import com.lyj.fakepivix.BR
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

    @get:Bindable
    var illust = Illust()
    set(value) {
        field = value
        notifyPropertyChanged(BR.illust)
    }

    var data: ObservableList<Illust> = ObservableArrayList()

    var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)
    var starState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var total = ObservableField(0)
    var current = ObservableField(1)
    var toolbarVisibility = ObservableField(true)
    // 悬浮标题是否显示
    var captionVisibility = ObservableField(false)

    //var star = ObservableField(false)

    val userFooterViewModel = UserFooterViewModel(this)
    val commentFooterViewModel = CommentFooterViewModel(this)
    val relatedCaptionFooterViewModel = RelatedCaptionViewModel(this)
    val relatedDialogViewModel = RelatedDialogViewModel(this)

    var position = -1
        set(value) {
            field = value
            initData()
        }

    /**
     * 拿到对应页数的数据
     */
    private fun initData() {
        val illust = IllustRepository.instance.illustList[position]
        this.illust = illust
        if (illust.meta_pages.isNotEmpty()) {
            val list = illust.meta_pages.map {
                Illust(image_urls = it.image_urls)
            }
            data.addAll(list)
        } else {
            data.add(Illust(image_urls = ImageUrls(illust.meta_single_page.original_image_url)))
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

    /**
     * 收藏/取消收藏
     */
    fun star() {
        if (starState.get() is LoadState.Loading)
            return
        val star = illust.is_bookmarked
        val disposable = IllustRepository.instance
                .star(illust.id.toString(), !star)
                .doOnSubscribe { starState.set(LoadState.Loading) }
                .subscribeBy(onNext = {
                    starState.set(LoadState.Succeed)
                    illust.is_bookmarked = !star
                }, onError = {
                    starState.set(LoadState.Failed(it))
                })
        addDisposable(disposable)
    }

}