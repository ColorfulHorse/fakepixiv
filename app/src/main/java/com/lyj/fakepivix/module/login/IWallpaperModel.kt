package com.lyj.fakepivix.module.login

import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.model.response.Illust
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