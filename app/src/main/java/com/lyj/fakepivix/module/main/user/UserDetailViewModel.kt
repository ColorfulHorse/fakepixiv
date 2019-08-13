package com.lyj.fakepivix.module.main.user

import android.arch.lifecycle.LifecycleOwner
import android.databinding.Bindable
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.model.response.UserInfo
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.data.source.remote.UserRepository
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.module.login.register.IRegisterModel
import com.lyj.fakepivix.module.login.register.RegisterModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.awaitSingle
import kotlinx.coroutines.withContext

/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc 登录
 */
class UserDetailViewModel : BaseViewModel<IModel?>() {

    override var mModel: IModel? = null

    var userId: String = ""
    val loadState = ObservableField<LoadState>(LoadState.Idle)
    @get:Bindable
    var userInfo = UserInfo()
    set(value) {
        field = value
        notifyPropertyChanged(BR.userInfo)
    }
    val showMore = ObservableField(false)

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

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        loadUserInfo()
    }

    fun loadUserInfo() {
        launch(Dispatchers.Main + CoroutineExceptionHandler { _, err ->
            loadState.set(LoadState.Failed(err))
        }) {
            loadState.set(LoadState.Loading)
            val resp = withContext(Dispatchers.IO) {
                UserRepository
                        .instance.getUserInfo(userId)
            }
            userInfo = resp
            loadState.set(LoadState.Succeed)
            if (resp.user.comment.isBlank()) {
                showMore.set(true)
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

    fun loadIllustWorks() {
        launch(Dispatchers.Main + CoroutineExceptionHandler { _, err ->
            illustWorksState.set(LoadState.Failed(err))
        }) {
            illustWorksState.set(LoadState.Loading)
            val resp = withContext(Dispatchers.IO) {
                IllustRepository
                        .instance.loadUserIllust(userId).awaitSingle()
            }
            illustWorks.clear()
            illustWorks.addAll(resp.illusts)
            illustWorksState.set(LoadState.Succeed)
        }
    }

    fun loadComicWorks() {
        launch(Dispatchers.Main + CoroutineExceptionHandler { _, err ->
            comicWorksState.set(LoadState.Failed(err))
        }) {
            comicWorksState.set(LoadState.Loading)
            val resp = IllustRepository
                        .instance.loadUserIllust(userId, IllustCategory.COMIC).awaitSingle()
            comicWorks.clear()
            comicWorks.addAll(resp.illusts)
            comicWorksState.set(LoadState.Succeed)
        }
    }

    fun loadNovelWorks() {
        launch(Dispatchers.Main + CoroutineExceptionHandler { _, err ->
            novelWorksState.set(LoadState.Failed(err))
        }) {
            novelWorksState.set(LoadState.Loading)
            val resp = withContext(Dispatchers.IO) {
                IllustRepository
                        .instance.loadUserNovels(userId)
            }
            novelWorks.clear()
            novelWorks.addAll(resp.illusts)
            novelWorksState.set(LoadState.Succeed)
        }
    }

    fun loadIllustBookmarks() {
        launch(Dispatchers.Main + CoroutineExceptionHandler { _, err ->
            illustBookmarksState.set(LoadState.Failed(err))
        }) {
            illustBookmarksState.set(LoadState.Loading)
            val resp = withContext(Dispatchers.IO) {
                IllustRepository
                        .instance.loadUserBookmarks(userId)
            }
            illustBookmarks.clear()
            illustBookmarks.addAll(resp.illusts)
            illustBookmarksState.set(LoadState.Succeed)
        }
    }

    fun loadNovelBookmarks() {
        launch(Dispatchers.Main + CoroutineExceptionHandler { _, err ->
            novelBookmarksState.set(LoadState.Failed(err))
        }) {
            novelBookmarksState.set(LoadState.Loading)
            val resp = withContext(Dispatchers.IO) {
                IllustRepository
                        .instance.loadUserBookmarks(userId, IllustCategory.NOVEL)
            }
            novelBookmarks.clear()
            novelBookmarks.addAll(resp.illusts)
            novelBookmarksState.set(LoadState.Succeed)
        }
    }

}