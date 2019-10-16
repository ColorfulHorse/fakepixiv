package com.lyj.fakepixiv.module.login

import com.lyj.fakepixiv.app.base.IModel
import com.lyj.fakepixiv.app.data.model.response.Illust
import io.reactivex.Observable

/**
 * @author greensun
 *
 * @date 2019/3/16
 *
 * @desc 登录页滚动背景
 */
interface IWallpaperModel : IModel {

    fun getData(): Observable<List<Illust>>
}