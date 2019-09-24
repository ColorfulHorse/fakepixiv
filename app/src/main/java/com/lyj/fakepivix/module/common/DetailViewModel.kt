package com.lyj.fakepivix.module.common

import android.databinding.Bindable
import android.databinding.ObservableField
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.data.source.remote.UserRepository
import com.lyj.fakepivix.app.databinding.onPropertyChangedCallback
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.module.illust.detail.items.CommentFooterViewModel
import com.lyj.fakepivix.module.illust.detail.RelatedIllustDialogViewModel
import com.lyj.fakepivix.module.illust.detail.RelatedUserDialogViewModel
import com.lyj.fakepivix.module.illust.detail.items.UserFooterViewModel

/**
 * @author greensun
 *
 * @date 2019/8/14
 *
 * @desc 小说插画详情
 */
open class DetailViewModel : BaseViewModel<IModel?>() {
    var key: Int = -1
    var position: Int = -1

    override val mModel: IModel? = null

    var liveData = Illust()

    @get: Bindable
    var illust = Illust()
    set(value) {
        field = value
        relatedUserViewModel.user = value.user
        notifyPropertyChanged(BR.illust)
    }

    open var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)
    var starState: ObservableField<LoadState> = ObservableField(LoadState.Idle)
    var followState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var total = ObservableField(0)

    @get:Bindable
    var current = 1
        set(value) {
            field = value
            notifyPropertyChanged(BR.current)
        }

    var toolbarVisibility = ObservableField(true)
    // 悬浮标题是否显示
    var captionVisibility = ObservableField(false)

    //var starIllust = ObservableField(false)

    val userFooterViewModel = UserFooterViewModel(this)
    val commentFooterViewModel = CommentFooterViewModel(this)
    val relatedIllustViewModel = RelatedIllustDialogViewModel(this)
    val relatedUserViewModel = RelatedUserDialogViewModel(illust.user)

    open fun setData(key: Int, position: Int) {
        this.key = key
        this.position = position
        liveData = IllustRepository.instance[key][position]
        illust = liveData.copy()
    }

    init {
        this + userFooterViewModel + commentFooterViewModel + relatedIllustViewModel + relatedUserViewModel
        starState.addOnPropertyChangedCallback(onPropertyChangedCallback { _, _ ->
            val state = starState.get()
            if (state is LoadState.Succeed) {
                val star = illust.is_bookmarked
                if (star) {
                    // 收藏成功加载弹出窗数据
                    relatedIllustViewModel.load()
                }
            }
        })
        followState.addOnPropertyChangedCallback(onPropertyChangedCallback { _, _ ->
            val state = followState.get()
            if (state is LoadState.Succeed) {
                if (liveData.user.is_followed) {
                    // 关注成功加载弹出窗数据
                    relatedUserViewModel.load()
                }
            }
        })
    }


    /**
     * 收藏/取消收藏
     */
    fun star() {
        val disposable = IllustRepository.instance
                .star(liveData, starState)
        disposable?.let {
            addDisposable(it)
        }
    }


    /**
     * 关注/取消关注
     */
    fun follow() {
        val disposable = UserRepository.instance
                .follow(liveData.user, followState)
        disposable?.let {
            addDisposable(it)
        }
    }

}