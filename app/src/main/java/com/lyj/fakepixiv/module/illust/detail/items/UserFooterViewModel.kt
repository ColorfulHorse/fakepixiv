package com.lyj.fakepixiv.module.illust.detail.items

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.lyj.fakepixiv.app.base.BaseViewModel

import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.data.source.remote.IllustRepository
import com.lyj.fakepixiv.app.data.source.remote.UserRepository
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.module.common.DetailViewModel
import kotlinx.coroutines.*

/**
 * @author greensun
 *
 * @date 2019/6/3
 *
 * @desc 详情页用户item
 */
class UserFooterViewModel(val parent: DetailViewModel) : BaseViewModel() {


    var data = ObservableArrayList<Illust>()

    var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    val followState: ObservableField<LoadState> = ObservableField(LoadState.Idle)


    fun load() {
        if (loadState.get() !is LoadState.Succeed) {
            reLoad()
        }
    }

    fun reLoad() {
        val type = parent.illust.type
        val p = parent.illust.user
        launch(CoroutineExceptionHandler { _, err ->
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

    /**
     * 关注/取消关注
     */
    fun follow() {
        val disposable = UserRepository.instance
                .follow(parent.liveData.user, followState)
        disposable?.let {
            addDisposable(it)
        }
    }
}