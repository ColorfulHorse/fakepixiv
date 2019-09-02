package com.lyj.fakepivix.module.illust.detail

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.app.utils.Router
import com.lyj.fakepivix.module.common.DetailViewModel
import io.reactivex.rxkotlin.subscribeBy

/**
 * @author greensun
 *
 * @date 2019/6/3
 *
 * @desc 相关作品
 */
class RelatedIllustDialogViewModel(val parent: DetailViewModel) : BaseViewModel<IModel?>() {
    override val mModel: IModel? = null

    var data = ObservableArrayList<Illust>()

    var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)


    fun load() {
        val disposable = IllustRepository.instance
                .loadRelatedIllust(parent.illust.id.toString())
                .doOnSubscribe { loadState.set(LoadState.Loading) }
                .subscribeBy(onNext = {
                    data.addAll(it.illusts)
                    loadState.set(LoadState.Succeed)
                    showDialog()
                }, onError = {
                    loadState.set(LoadState.Failed(it))
                })
        addDisposable(disposable)
    }

    private fun showDialog() {
        Router.getTopFragmentManager()?.let {
            // 数据加载完成弹出dialog
            val dialogFragment = RelatedIllustDialogFragment.newInstance()
            dialogFragment.mViewModel = this
            dialogFragment.show(it, "RelatedIllustDialogFragment")
        }
    }
}