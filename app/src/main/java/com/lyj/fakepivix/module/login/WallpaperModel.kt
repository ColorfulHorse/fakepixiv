package com.lyj.fakepivix.module.login;

import com.lyj.fakepivix.app.base.BaseModel
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.network.ApiException
import com.lyj.fakepivix.app.reactivex.schedulerTransform
import io.reactivex.Observable

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
                .map {
                    if (it.illusts.isNotEmpty()) {
                        return@map it.illusts
                    }
                    throw ApiException(ApiException.CODE_EMPTY_DATA)
                }
                .schedulerTransform()
    }

}
