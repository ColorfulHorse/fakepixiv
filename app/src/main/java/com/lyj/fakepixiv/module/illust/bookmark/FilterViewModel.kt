package com.lyj.fakepixiv.module.illust.bookmark

import androidx.databinding.Bindable
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.library.baseAdapters.BR
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.application.ApplicationLike
import com.lyj.fakepixiv.app.base.BaseViewModel

import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.constant.Restrict
import com.lyj.fakepixiv.app.data.model.response.BookmarkTag
import com.lyj.fakepixiv.app.data.source.remote.IllustRepository
import com.lyj.fakepixiv.app.network.LoadState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author greensun
 *
 * @date 2019/9/19
 *
 * @desc
 */
class FilterViewModel : BaseViewModel() {


    var category: String = IllustCategory.ILLUST

    @get:Bindable
    var isPub = true
    set(value) {
        field = value
        notifyPropertyChanged(BR.pub)
    }

    var privateLoadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var publicLoadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    val publicTags = ObservableArrayList<BookmarkTag>()

    val privateTags = ObservableArrayList<BookmarkTag>()

    var publicTag = ""
    var privateTag = ""

//    override fun onCreate(owner: LifecycleOwner) {
//        super.onCreate(owner)
//        privateLoadState = ObservableField(LoadState.Idle)
//        publicLoadState = ObservableField(LoadState.Idle)
//    }

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
            val tags = listOf(BookmarkTag(ApplicationLike.context.getString(R.string.all)), BookmarkTag(ApplicationLike.context.getString(R.string.other))) + resp.bookmark_tags
            tags.first { it.name == publicTag }.selected = true
            publicTags.addAll(tags)
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
            val tags = listOf(BookmarkTag(ApplicationLike.context.getString(R.string.all)), BookmarkTag(ApplicationLike.context.getString(R.string.other))) + resp.bookmark_tags
            tags.first { it.name == privateTag }.selected = true
            privateTags.addAll(tags)
            privateLoadState.set(LoadState.Succeed)
        }
    }

    fun load(position: Int) {
        isPub = position == 0
        if (position == 0) {
            loadPublic()
        } else {
            loadPrivate()
        }
    }
}