package com.lyj.fakepivix.module.main.novel

import android.arch.lifecycle.LifecycleOwner
import android.databinding.Bindable
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.app.data.model.response.NovelText
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.module.common.DetailViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author greensun
 *
 * @date 2019/5/30
 *
 * @desc 小说详情
 */
class NovelDetailViewModel(key: Int, position: Int) : DetailViewModel(key, position) {

    var data: ObservableList<String> = ObservableArrayList()

    @get:Bindable
    var showPageNum = false
    set(value) {
        field = value
        //notifyPropertyChanged(BR.showPageNum)
    }

    var novelText: NovelText? = null

    init {
        captionVisibility.set(true)
    }

    override fun onStart(owner: LifecycleOwner) {
        load()
    }


    fun load() {
        launch(Dispatchers.Main + CoroutineExceptionHandler { _, err ->
            loadState.set(LoadState.Failed(err))
        }) {
            loadState.set(LoadState.Loading)
            val resp = withContext(Dispatchers.IO) {
                IllustRepository.instance
                        .getNovelText(illust.id.toString())
            }
            val content = resp.getNovel()
            total.set(content.size)
            data.addAll(content)
            novelText = resp
            loadState.set(LoadState.Succeed)
        }
    }

}