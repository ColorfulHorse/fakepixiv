package com.lyj.fakepivix.module.login

import android.arch.lifecycle.LifecycleOwner
import android.databinding.Bindable
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.util.ViewPreloadSizeProvider
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingAdapter
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.model.response.Illust
import com.lyj.fakepivix.databinding.ItemWallpaperBinding
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc 登录页滚动背景
 */
class WallpaperViewModel : BaseViewModel<IWallpaperModel>() {

    override var mModel: IWallpaperModel = WallpaperModel()

    val data = ObservableArrayList<Illust>()

    var overlayVisibility = ObservableField<Boolean>(true)


    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        val disposable = mModel.getData()
                .subscribeBy(onNext = {
                    data.addAll(it)
                }, onError = {
                    Timber.e(it.message)
                })
        addDisposable(disposable)

    }

}