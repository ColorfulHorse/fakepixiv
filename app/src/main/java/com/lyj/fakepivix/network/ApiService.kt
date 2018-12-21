package com.lyj.fakepivix.network

import com.lyj.fakepivix.model.response.IllustResp
import com.lyj.fakepivix.model.response.IllustListResp
import com.lyj.fakepivix.model.response.MemberResp
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

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
     * 请求排行榜
     * mode: 排行方式
     * page: 页数
     * date: yy-MM-dd字符串 默认昨天
     */
    @GET("?type=rank")
    fun getRank(@Query("mode") mode: String = RANK_MODE_WEEK, @Query("page") page: Int = 1, @Query("date") date: String = ""): Observable<IllustListResp>

    /**
     * 作品详情
     */
    @GET("?type=illust")
    fun getIllust(@Query("id") id: Int): Observable<IllustResp>

    /**
     * 用户详情
     */
    @GET("?type=member")
    fun getUserInfo(@Query("id") id: Int): Observable<MemberResp>

    /**
     * 用户作品详情列表
     */
    @GET("?type=member_illust")
    fun getUserIllusts(@Query("id") id: Int, @Query("page") page: Int = 1): Observable<IllustListResp>

    /**
     * 用户收藏列表
     */
    @GET("?type=favorite")
    fun getUserStar(@Query("id") id: Int, @Query("page") page: Int = 1): Observable<IllustListResp>
}