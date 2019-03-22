package com.lyj.fakepivix.module.login

import android.arch.lifecycle.LifecycleOwner
import android.databinding.Bindable
import android.support.v7.widget.GridLayoutManager
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.model.response.Illust
import io.reactivex.rxkotlin.subscribeBy
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingAdapter
import com.lyj.fakepivix.databinding.ItemWallpaperBinding
import io.reactivex.disposables.Disposable

/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc 登录页滚动背景
 */
class WallpaperViewModel : BaseViewModel<IWallpaperModel>() {

    override var mModel: IWallpaperModel = WallpaperModel()

    @get:Bindable
    var data: MutableList<Illust> = mutableListOf()

    val adapter = object : BaseBindingAdapter<Illust, BaseBindingAdapter.BaseBindingViewHolder<ItemWallpaperBinding>>(R.layout.item_wallpaper) {

        override fun convert(helper: BaseBindingViewHolder<ItemWallpaperBinding>, item: Illust) {
            helper.binding?.illust = item
        }
    }

    private fun update(list: MutableList<Illust>) {
        data.clear()
        data.addAll(list)
        notifyPropertyChanged(BR.data)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        val disposable = mModel.getData()
                .subscribeBy(onNext = {
                    update(it)
                }, onError = {

                })
        addDisposable(disposable)
    }

}