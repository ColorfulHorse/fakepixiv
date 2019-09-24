package com.lyj.fakepivix.module.illust.bookmark

import android.arch.lifecycle.LifecycleOwner
import android.databinding.Bindable
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.App
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.constant.Constant
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.constant.Restrict
import com.lyj.fakepivix.app.data.model.response.BookmarkTag
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.network.ApiException
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.app.utils.ToastUtil
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
            val tags = listOf(BookmarkTag(App.context.getString(R.string.all)), BookmarkTag(App.context.getString(R.string.other))) + resp.bookmark_tags
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
            val tags = listOf(BookmarkTag(App.context.getString(R.string.all)), BookmarkTag(App.context.getString(R.string.other))) + resp.bookmark_tags
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