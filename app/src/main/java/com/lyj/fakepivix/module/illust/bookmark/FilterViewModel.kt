package com.lyj.fakepivix.module.illust.bookmark

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.constant.Restrict
import com.lyj.fakepivix.app.data.model.response.BookmarkTag
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.network.ApiException
import com.lyj.fakepivix.app.network.LoadState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

/**
 * @author greensun
 *
 * @date 2019/9/19
 *
 * @desc
 */
class FilterViewModel : BaseViewModel<IModel?>() {
    override val mModel: IModel? = null

    var category: String = IllustCategory.ILLUST

    var isPublic = true

    var privateLoadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var publicLoadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    val publicTags = ObservableArrayList<BookmarkTag>()

    val privateTags = ObservableArrayList<BookmarkTag>()

    fun loadPublic() {
        if (publicLoadState.get() is LoadState.Succeed)
            return
        launch(CoroutineExceptionHandler { _, err ->
            publicLoadState.set(LoadState.Failed(err))
        }) {
            publicLoadState.set(LoadState.Loading)
            publicTags.clear()
            val resp = withContext(Dispatchers.IO) {
                IllustRepository.instance
                        .getBookMarkTags(category)
            }
            publicTags.addAll(resp.bookmark_tags.plus(BookmarkTag("全部")))
            publicLoadState.set(LoadState.Succeed)
        }
    }

    fun loadPrivate() {
        if (privateLoadState.get() is LoadState.Succeed)
            return
        launch(CoroutineExceptionHandler { _, err ->
            privateLoadState.set(LoadState.Failed(err))
        }) {
            privateLoadState.set(LoadState.Loading)
            privateTags.clear()
            val resp = withContext(Dispatchers.IO) {
                IllustRepository.instance
                        .getBookMarkTags(category, Restrict.PRIVATE)
            }

            privateTags.addAll(resp.bookmark_tags.plus(BookmarkTag("全部")))
            privateLoadState.set(LoadState.Succeed)
        }
    }

    fun load(position: Int) {
        isPublic = position == 0
        if (position == 0) {
            loadPublic()
        } else {
            loadPrivate()
        }
    }
}