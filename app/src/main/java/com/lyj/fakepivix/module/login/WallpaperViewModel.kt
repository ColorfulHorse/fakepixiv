package com.lyj.fakepivix.module.login

import android.arch.lifecycle.LifecycleOwner
import android.databinding.Bindable
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.model.response.Illust
import io.reactivex.rxkotlin.subscribeBy
import com.lyj.fakepivix.BR
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

    fun update(list: MutableList<Illust>) {
        data.clear()
        data.addAll(list)
        notifyPropertyChanged(BR.data)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        mModel.getData()
                .subscribeBy(onNext = {
                    update(it)
                }, onError = {

                })
    }

}