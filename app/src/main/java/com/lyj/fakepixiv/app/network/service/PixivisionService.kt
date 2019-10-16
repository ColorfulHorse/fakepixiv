package com.lyj.fakepixiv.app.network.service

import com.lyj.fakepixiv.app.constant.Constant
import com.lyj.fakepixiv.app.data.model.response.LoginResp
import com.lyj.fakepixiv.app.data.model.response.SpotLightResp
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
interface PixivisionService {
    /**
     * 主页特辑
     */
    @GET("/v1/spotlight/articles")
    suspend fun getIllustPixivisionData(@Query("category") category: String): SpotLightResp

    @GET
    suspend fun getMoretPixivision(@Url url: String): SpotLightResp
}