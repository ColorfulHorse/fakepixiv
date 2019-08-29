package com.lyj.fakepivix.module.novel

import android.databinding.Bindable
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.data.model.response.NovelChapter
import com.lyj.fakepivix.app.data.model.response.NovelText
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.app.utils.ToastUtil
import com.lyj.fakepivix.module.common.DetailViewModel
import kotlinx.coroutines.*
import timber.log.Timber

/**
 * @author greensun
 *
 * @date 2019/5/30
 *
 * @desc 小说详情
 */
class NovelDetailViewModel : DetailViewModel() {

    var novelChapter: NovelChapter? = null
    set(value) {
        field = value
        value?.let {
            illust = illust.copy(caption = it.caption, id = it.id, tags = it.tags,
                    image_urls = it.image_urls, create_date = it.create_date, total_bookmarks = it.total_bookmarks,
                    total_view = it.total_view, title = it.title, series = it.series)
        }
    }

    var data: ObservableList<String> = ObservableArrayList()

    // 滑动时显示页码
    @get:Bindable
    var showPageNum = false
    set(value) {
        field = value
        notifyPropertyChanged(BR.showPageNum)
    }

    // 点击显示覆盖物
    @get:Bindable
    var showOverlay = true
        set(value) {
            field = value
            notifyPropertyChanged(BR.showOverlay)
        }

    var novelText: NovelText? = null
    var markState = ObservableField<LoadState>(LoadState.Idle)

    fun load() {
        launch(CoroutineExceptionHandler { _, err ->
            Timber.e(Thread.currentThread().name)
            loadState.set(LoadState.Failed(err))
        }) {
            loadState.set(LoadState.Loading)
            val resp = withContext(Dispatchers.IO) {
                //throw Exception("xxx")
                IllustRepository.instance
                        .getNovelText(illust.id.toString())
            }
            val content = resp.getNovel()
            total.set(content.size)
            resp.novel_marker.page?.let {
                current = it
            }
            data.addAll(content)
            novelText = resp
            loadState.set(LoadState.Succeed)
        }
    }

    /**
     * 添加书签
     */
    fun mark() {
        if (markState.get() is LoadState.Loading)
            return
        launch(Dispatchers.Main + CoroutineExceptionHandler { context, err ->
            markState.set(LoadState.Failed(err))
        }) {
            markState.set(LoadState.Loading)
            val marked = novelText?.novel_marker?.page != null
            withContext(Dispatchers.IO) {
                if (!marked) {
                    IllustRepository
                            .instance
                            .addNovelMarker(illust.id.toString(), current)
                }else {
                    IllustRepository
                            .instance
                            .deleteNovelMarker(illust.id.toString())
                }
            }
            if (marked) {
                novelText?.novel_marker?.page = null
                ToastUtil.showToast(R.string.unmark)
            }else {
                novelText?.novel_marker?.page = current
                ToastUtil.showToast(R.string.mark_page, current)
            }
            markState.set(LoadState.Succeed)
        }
    }

}