package com.lyj.fakepixiv.module.main.home.illust

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.base.IModel
import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.data.model.response.SpotlightArticle
import com.lyj.fakepixiv.app.data.source.remote.PixivisionRepository
import com.lyj.fakepixiv.app.network.LoadState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc 特辑
 */
class PixivisionViewModel : BaseViewModel<IModel?>() {
    override val mModel: IModel? = null

    var category = IllustCategory.ILLUST

    val data = ObservableArrayList<SpotlightArticle>()
    private var nextUrl = ""

    var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var loadMoreState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    fun load() {
        launch(CoroutineExceptionHandler { _, err ->
            loadState.set(LoadState.Failed(err))
        }) {
            loadState.set(LoadState.Loading)
            val resp = withContext(Dispatchers.IO) {
                PixivisionRepository.instance
                        .loadRecommend(category)
            }
            data.clear()
            data.addAll(resp.spotlight_articles)
            nextUrl = resp.next_url
            loadState.set(LoadState.Succeed)
        }
    }

    fun loadMore() {
        launch(CoroutineExceptionHandler { _, err ->
            loadMoreState.set(LoadState.Failed(err))
        }) {
            loadMoreState.set(LoadState.Loading)
            val resp = withContext(Dispatchers.IO) {
                PixivisionRepository.instance
                        .loadMore(nextUrl)
            }
            data.addAll(resp.spotlight_articles)
            nextUrl = resp.next_url
            loadMoreState.set(LoadState.Succeed)
        }
    }
}