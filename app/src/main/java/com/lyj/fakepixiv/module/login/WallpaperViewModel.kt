package com.lyj.fakepixiv.module.login

import android.arch.lifecycle.LifecycleOwner
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.data.source.remote.IllustRepository
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc 登录页滚动背景
 */
class WallpaperViewModel : BaseViewModel() {

    val data = ObservableArrayList<Illust>()

    var overlayVisibility = ObservableField<Boolean>(true)


    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        val disposable = IllustRepository.instance
                .getWallPaperData()
                .subscribeBy(onNext = {
                    data.addAll(it.illusts)
                }, onError = {
                    Timber.e(it.message)
                })
        addDisposable(disposable)
    }

}