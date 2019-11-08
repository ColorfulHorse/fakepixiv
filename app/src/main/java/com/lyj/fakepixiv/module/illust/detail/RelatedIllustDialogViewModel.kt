package com.lyj.fakepixiv.module.illust.detail

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.lyj.fakepixiv.app.base.BaseViewModel

import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.data.source.remote.IllustRepository
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.app.utils.Router
import com.lyj.fakepixiv.module.common.DetailViewModel
import io.reactivex.rxkotlin.subscribeBy

/**
 * @author greensun
 *
 * @date 2019/6/3
 *
 * @desc 相关作品
 */
class RelatedIllustDialogViewModel(val parent: DetailViewModel) : BaseViewModel() {


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