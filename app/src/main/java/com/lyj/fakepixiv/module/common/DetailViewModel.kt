package com.lyj.fakepixiv.module.common

import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.LifecycleOwner
import com.lyj.fakepixiv.app.base.BaseViewModel

import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.data.source.remote.IllustRepository
import com.lyj.fakepixiv.app.data.source.remote.UserRepository
import com.lyj.fakepixiv.app.databinding.onPropertyChangedCallback
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.app.utils.Router
import com.lyj.fakepixiv.module.illust.detail.items.CommentListViewModel
import com.lyj.fakepixiv.module.illust.detail.RelatedIllustDialogViewModel
import com.lyj.fakepixiv.module.illust.detail.RelatedUserDialogViewModel
import com.lyj.fakepixiv.module.illust.detail.comment.InputViewModel
import com.lyj.fakepixiv.module.illust.detail.items.SeriesItemViewModel
import com.lyj.fakepixiv.module.illust.detail.items.UserFooterViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * @author greensun
 *
 * @date 2019/8/14
 *
 * @desc 小说插画详情
 */
open class DetailViewModel : BaseViewModel() {
    var key: Int = -1
    var position: Int = -1

    @get: Bindable
    var liveData = Illust()
        set(value) {
            field = value
            notifyPropertyChanged(BR.liveData)
        }

    /**
     * 由于小说的每个章节相当于独立作品，所以不能改变liveData的一些值，而是要单独copy变量来操作
     */
    @get: Bindable
    var illust = Illust()
    set(value) {
        field = value
        relatedUserViewModel.user = value.user
        commentListViewModel.illust = value
        notifyPropertyChanged(BR.illust)
    }

    open var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)
    var starState: ObservableField<LoadState> = ObservableField(LoadState.Idle)


    var total = ObservableField(0)

    // 当前页数
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

    // 用户信息vm
    val userFooterViewModel = UserFooterViewModel(this)
    // 评论列表vm
    val commentListViewModel = CommentListViewModel()
    // 相关作品vm
    val relatedIllustViewModel = RelatedIllustDialogViewModel(this)
    // 相关用户vm
    val relatedUserViewModel = RelatedUserDialogViewModel(illust.user)

    open val seriesItemViewModel: SeriesItemViewModel? = null

    /**
     * 初始化作品信息
     */
    fun setData(key: Int, position: Int) {
        this.key = key
        this.position = position
        setData(IllustRepository.instance[key][position])
    }

    open fun setData(data: Illust) {
        liveData = data
        illust = data.copy()
    }

    init {
        this + userFooterViewModel + commentListViewModel + relatedIllustViewModel + relatedUserViewModel
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
        userFooterViewModel.followState.addOnPropertyChangedCallback(onPropertyChangedCallback { _, _ ->
            val state = userFooterViewModel.followState.get()
            if (state is LoadState.Succeed) {
                if (liveData.user.is_followed) {
                    // 关注成功加载弹出窗数据
                    relatedUserViewModel.load()
                }
            }
        })
    }

    override fun lazyInit() {
        super.lazyInit()
        createHistory()
    }

    /**
     * 保存浏览历史
     */
    fun createHistory() {
        launch(CoroutineExceptionHandler { _, err ->
            Timber.e(err.toString())
        }) {
            withContext(Dispatchers.IO) {
                IllustRepository.instance
                        .saveHistory(illust)
            }
        }
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


    fun goUserDetail() {
        Router.goUserDetail(illust.user)
    }
}