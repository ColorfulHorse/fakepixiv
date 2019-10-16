package com.lyj.fakepixiv.app.data.source.remote

import com.lyj.fakepixiv.app.data.model.response.SpotLightResp
import com.lyj.fakepixiv.app.network.retrofit.RetrofitManager
import com.lyj.fakepixiv.app.network.service.LiveService
import com.lyj.fakepixiv.app.network.service.PixivisionService

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc
 */
class PixivisionRepository private constructor() {

    private val service: PixivisionService by lazy { RetrofitManager.instance.pixivisionService }

    companion object {
        val instance by lazy { PixivisionRepository() }
    }


    suspend fun loadRecommend(category: String): SpotLightResp {
        return service.getIllustPixivisionData(category = category)
    }

    suspend fun loadMore(url: String): SpotLightResp {
        return service.getMoretPixivision(url)
    }
}