package com.lyj.fakepivix.app.network


import com.lyj.fakepivix.app.data.model.response.IllustListResp
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * @author 19930
 *
 * @date 2018/12/21
 *
 * @desc power by https://api.imjad.cn/pixiv_v2.md
 */
interface ApiService {
    companion object {
        const val RANK_MODE_DAY = "day"
        const val RANK_MODE_WEEK = "week"
        const val RANK_MODE_MONTH = "month"
        // 新秀
        const val RANK_MODE_ROOKIE = "week_rookie"
        // 原创
        const val RANK_MODE_ORIGINAL = "week_original"
        //男性向
        const val RANK_MODE_MALE = "day_male"
        const val RANK_MODE_FAMALE = "day_female"
    }

    /**
     * 登录页滚动图片墙
     */
    @GET("/v1/walkthrough/illusts")
    fun getWallPaperData(): Observable<IllustListResp>

}