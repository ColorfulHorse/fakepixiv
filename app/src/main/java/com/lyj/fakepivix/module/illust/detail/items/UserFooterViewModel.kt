package com.lyj.fakepivix.module.illust.detail.items

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.module.common.DetailViewModel
import kotlinx.coroutines.*

/**
 * @author greensun
 *
 * @date 2019/6/3
 *
 * @desc 详情页用户item
 */
class UserFooterViewModel(val parent: DetailViewModel) : BaseViewModel<IModel?>() {
    override val mModel: IModel? = null

    var data = ObservableArrayList<Illust>()

    var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)


    fun load() {
        if (loadState.get() is LoadState.Idle) {
            reLoad()
        }
    }

    fun reLoad() {
        val type = parent.illust.type
        val p = parent.illust.user
        launch(Dispatchers.Main + CoroutineExceptionHandler { _, err ->
            loadState.set(LoadState.Failed(err))
        }) {
            loadState.set(LoadState.Loading)
            val resp = withContext(Dispatchers.IO) {
                when(type) {
                    IllustCategory.ILLUST -> IllustRepository.instance.loadUserIllust(p.id)
                    IllustCategory.COMIC -> IllustRepository.instance.loadUserIllust(p.id, IllustCategory.COMIC)
                    IllustCategory.NOVEL -> IllustRepository.instance.loadUserNovels(p.id)
                    else -> IllustRepository.instance.loadUserIllust(p.id)
                }
            }
            loadState.set(LoadState.Succeed)
            data.clear()
            data.addAll(resp.illusts.take(3))
        }
    }
}