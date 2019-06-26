package com.lyj.fakepivix.module.main.illust

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.model.response.User
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.network.LoadState
import io.reactivex.rxkotlin.subscribeBy

/**
 * @author greensun
 *
 * @date 2019/6/3
 *
 * @desc 详情页用户item
 */
class UserFooterViewModel(val parent: IllustDetailViewModel) : BaseViewModel<IModel?>() {
    override val mModel: IModel? = null

    var data = ObservableArrayList<Illust>()

    var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    val illust = parent.illust

    fun load() {
        if (loadState.get() is LoadState.Idle) {
            reLoad()
        }
    }

    fun reLoad() {
        val p = parent.illust.user
        if (p != null) {
            val disposable = IllustRepository.instance
                    .loadUserIllust(p.id)
                    .doOnSubscribe {
                        data.clear()
                        loadState.set(LoadState.Loading)
                    }
                    .subscribeBy(onNext = {
                        loadState.set(LoadState.Succeed)
                        data.clear()
                        data.addAll(it.illusts.take(3))
                    }, onError = {
                        loadState.set(LoadState.Failed(it))
                    })
            addDisposable(disposable)
        }
    }
}