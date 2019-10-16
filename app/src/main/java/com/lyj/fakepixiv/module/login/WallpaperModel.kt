package com.lyj.fakepixiv.module.login;

import com.lyj.fakepixiv.app.base.BaseModel
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.data.source.remote.IllustRepository
import com.lyj.fakepixiv.app.network.ApiException
import com.lyj.fakepixiv.app.reactivex.schedulerTransform
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
        return IllustRepository.instance.service.getWallPaperData()
                .map {
                    if (it.illusts.isNotEmpty()) {
                        return@map it.illusts
                    }
                    throw ApiException(ApiException.CODE_EMPTY_DATA)
                }
                .schedulerTransform()
    }

}
