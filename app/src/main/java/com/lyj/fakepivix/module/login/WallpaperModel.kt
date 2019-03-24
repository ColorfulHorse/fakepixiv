package com.lyj.fakepivix.module.login;

import com.lyj.fakepivix.app.base.BaseModel
import com.lyj.fakepivix.app.model.response.Illust
import com.lyj.fakepivix.app.network.ApiException
import com.lyj.fakepivix.module.login.register.IRegisterModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author greensun
 *
 * @date 2019/3/16
 *
 * @desc 登录页滚动背景Model
 */
class WallpaperModel : IWallpaperModel, BaseModel() {

    override fun getData(): Observable<List<Illust>> {
        return mApi.getWallPaperData()
                .compose(applyScheduler())
                .map {
                    if (!it.illusts.isEmpty()) {
                        return@map it.illusts
                    }
                    throw ApiException(ApiException.CODE_EMPTY_DATA)
                }
    }

}
