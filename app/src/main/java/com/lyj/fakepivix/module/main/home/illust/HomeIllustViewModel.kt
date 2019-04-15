package com.lyj.fakepivix.module.main.home.illust

import android.arch.lifecycle.LifecycleOwner
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.module.login.register.IRegisterModel
import com.lyj.fakepivix.module.login.register.RegisterModel

/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc 登录
 */
class HomeIllustViewModel : BaseViewModel<IHomeIllustModel>() {

    override var mModel: IHomeIllustModel = HomeIllustModel()

    val rankViewModel: RankViewModel = RankViewModel()
    val liveViewModel: LiveViewModel = LiveViewModel()
    val pixivisionViewModel: PixivisionViewModel = PixivisionViewModel()

    val data = ObservableArrayList<Illust>()

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

    }

    fun lazyLoad() {
        liveViewModel.load()
        pixivisionViewModel.load()

    }

}