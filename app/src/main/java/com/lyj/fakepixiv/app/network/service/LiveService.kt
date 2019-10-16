package com.lyj.fakepixiv.app.network.service

import com.lyj.fakepixiv.app.constant.Constant
import com.lyj.fakepixiv.app.data.model.response.LiveListResp
import com.lyj.fakepixiv.app.data.model.response.LoginResp
import com.lyj.fakepixiv.app.network.retrofit.interceptors.SwitchBaseUrlInterceptor
import io.reactivex.Observable
import retrofit2.http.*

/**
 * @author green sun
 *
 * @date 2019/10/16
 *
 * @desc
 */
interface LiveService {
    /**
     * 直播列表  [type] following:关注的直播
     */
    @GET("/v1/live/list")
    fun getLiveListData(@Query("list_type") type: String = "popular"): Observable<LiveListResp>
}