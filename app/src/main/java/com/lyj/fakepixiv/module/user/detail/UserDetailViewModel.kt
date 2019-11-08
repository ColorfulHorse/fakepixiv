package com.lyj.fakepixiv.module.user.detail

import androidx.databinding.Bindable
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.lyj.fakepixiv.BR
import com.lyj.fakepixiv.app.base.BaseViewModel

import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.data.model.response.User
import com.lyj.fakepixiv.app.data.model.response.UserInfo
import com.lyj.fakepixiv.app.data.source.remote.IllustRepository
import com.lyj.fakepixiv.app.data.source.remote.UserRepository
import com.lyj.fakepixiv.app.databinding.onPropertyChangedCallback
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.module.illust.detail.RelatedUserDialogViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc 登录
 */
class UserDetailViewModel : BaseViewModel() {



    var userId: String = ""
    set(value) {
        field = value
        user = UserRepository.instance[userId]
    }

    @get:Bindable
    var user: User? = null
    set(value) {
        field = value
        value?.let {
            relatedUserViewModel.user = it
            notifyPropertyChanged(BR.user)
            isSelf = it.id == UserRepository.instance.loginData?.user?.id
        }
    }

    @get:Bindable
    var isSelf = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.self)
        }

    val loadState = ObservableField<LoadState>(LoadState.Idle)

    @get:Bindable
    var userInfo = UserInfo()
    set(value) {
        field = value
        notifyPropertyChanged(BR.userInfo)
    }

    /**
     * 用户介绍是否显示完整
     */
    @get:Bindable
    var showMore = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.showMore)
        }

    // toolbar是否收缩
    val collapsed = ObservableField(false)

    val followState = ObservableField<LoadState>(LoadState.Idle)

    val illustWorksState = ObservableField<LoadState>(LoadState.Idle)
    val comicWorksState = ObservableField<LoadState>(LoadState.Idle)
    val novelWorksState = ObservableField<LoadState>(LoadState.Idle)
    val illustBookmarksState = ObservableField<LoadState>(LoadState.Idle)
    val novelBookmarksState = ObservableField<LoadState>(LoadState.Idle)
    // 作品
    val illustWorks = ObservableArrayList<Illust>()
    val comicWorks = ObservableArrayList<Illust>()
    val novelWorks = ObservableArrayList<Illust>()
    // 收藏
    val illustBookmarks = ObservableArrayList<Illust>()
    val novelBookmarks = ObservableArrayList<Illust>()
    val workspace = ObservableArrayList<Pair<String, String>>()

    // 相关作者
    val relatedUserViewModel = RelatedUserDialogViewModel()


    init {
        this + relatedUserViewModel
        followState.addOnPropertyChangedCallback(onPropertyChangedCallback { _, _ ->
            val state = followState.get()
            if (state is LoadState.Succeed) {
                user?.let {
                    if (it.is_followed) {
                        // 关注成功加载弹出窗数据
                        relatedUserViewModel.load()
                    }
                }
            }
        })
    }

    /**
     * 获取用户信息
     */
    fun loadUserInfo() {
        launch(CoroutineExceptionHandler { _, err ->
            loadState.set(LoadState.Failed(err))
        }) {
            loadState.set(LoadState.Loading)
            val resp = withContext(Dispatchers.IO) {
                UserRepository
                        .instance.getUserInfo(userId)
            }
            userInfo = resp
            if (user == null) {
                user = userInfo.user
            }
            workspace.addAll(resp.workspace.getTextList())
            loadState.set(LoadState.Succeed)
            if (resp.user.comment.isBlank()) {
                showMore = true
            }
            with(resp.profile) {
                if (total_illusts > 0) {
                    loadIllustWorks()
                }
                if (total_manga > 0) {
                    loadComicWorks()
                }
                if (total_novels > 0) {
                    loadNovelWorks()
                }
                if (total_illust_bookmarks_public > 0) {
                    loadIllustBookmarks()
                }
                loadNovelBookmarks()
            }
        }
    }

    /**
     * 用户插画作品
     */
    fun loadIllustWorks() {
        launch(CoroutineExceptionHandler { _, err ->
            illustWorksState.set(LoadState.Failed(err))
        }) {
            illustWorksState.set(LoadState.Loading)
            val resp = withContext(Dispatchers.IO) {
                IllustRepository
                        .instance.loadUserIllust(userId)
            }
            illustWorks.clear()
            illustWorks.addAll(resp.illusts.take(6).toMutableList())
            illustWorksState.set(LoadState.Succeed)
        }
    }

    fun loadComicWorks() {
        launch(CoroutineExceptionHandler { _, err ->
            comicWorksState.set(LoadState.Failed(err))
        }) {
            comicWorksState.set(LoadState.Loading)
            val resp = IllustRepository
                        .instance.loadUserIllust(userId, IllustCategory.COMIC)
            comicWorks.clear()
            comicWorks.addAll(resp.illusts.take(2).toMutableList())
            comicWorksState.set(LoadState.Succeed)
        }
    }

    fun loadNovelWorks() {
        launch(CoroutineExceptionHandler { _, err ->
            novelWorksState.set(LoadState.Failed(err))
        }) {
            novelWorksState.set(LoadState.Loading)
            val resp = withContext(Dispatchers.IO) {
                IllustRepository
                        .instance.loadUserNovels(userId)
            }
            novelWorks.clear()
            novelWorks.addAll(resp.illusts.take(3).toMutableList())
            novelWorksState.set(LoadState.Succeed)
        }
    }

    /**
     * 用户收藏插画-漫画
     */
    fun loadIllustBookmarks() {
        launch(CoroutineExceptionHandler { _, err ->
            illustBookmarksState.set(LoadState.Failed(err))
        }) {
            illustBookmarksState.set(LoadState.Loading)
            val resp = withContext(Dispatchers.IO) {
                IllustRepository
                        .instance.loadUserBookmarks(userId)
            }
            illustBookmarks.clear()
            illustBookmarks.addAll(resp.illusts.filter { it.visible }.take(6).toMutableList())
            illustBookmarksState.set(LoadState.Succeed)
        }
    }

    fun loadNovelBookmarks() {
        launch(CoroutineExceptionHandler { _, err ->
            novelBookmarksState.set(LoadState.Failed(err))
        }) {
            novelBookmarksState.set(LoadState.Loading)
            val resp = withContext(Dispatchers.IO) {
                IllustRepository
                        .instance.loadUserBookmarks(userId, IllustCategory.NOVEL)
            }
            novelBookmarks.clear()
            novelBookmarks.addAll(resp.illusts.take(3).toMutableList())
            novelBookmarksState.set(LoadState.Succeed)
        }
    }

    fun follow() {
        user?.let {
            val disposable = UserRepository.instance
                    .follow(it, followState)
            disposable?.let { d ->
                addDisposable(d)
            }
        }
    }

    fun showMore(){
        showMore = !showMore
    }
}