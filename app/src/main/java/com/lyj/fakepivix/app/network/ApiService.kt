package com.lyj.fakepivix.app.network


import com.lyj.fakepivix.app.constant.Category
import com.lyj.fakepivix.app.constant.Constant
import com.lyj.fakepivix.app.constant.ILLUST
import com.lyj.fakepivix.app.constant.NOVEL
import com.lyj.fakepivix.app.data.model.response.*
import com.lyj.fakepivix.app.network.retrofit.interceptors.SwitchBaseUrlInterceptor
import io.reactivex.Observable
import retrofit2.http.*

/**
 * @author 19930
 *
 * @date 2018/12/21
 *
 * @desc
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

    /**
     * 登录
     * header 用于切换baseUrl [SwitchBaseUrlInterceptor]
     * [grantType] 登录方式，账号密码或refreshToken登录二选一
     */
    @Headers("SWITCH-HEADER:TAG_AUTH")
    @POST("/auth/token")
    @FormUrlEncoded
    fun login(@Field("client_id") clientId: String = "MOBrBDS8blbauoSck0ZfDbtuzpyT", @Field("client_secret")clientSecret: String = "lsACyCD94FhDUtGTXi3QzcFE2uU1hqtDaKeqrdwj",
              @Field("get_secure_url")get_secure_url: Boolean = true, @Field("include_policy")include_policy: Boolean = true, @Field("grant_type")grantType: String = Constant.Net.GRANT_TYPE_PWD,
              @Field("username")userName: String = "", @Field("password")password: String = "", @Field("device_token")deviceToken: String = "", @Field("refresh_token")refreshToken: String = ""): Observable<LoginResp>


    /**
     * 主页直播
     */
    @GET("/v1/live/list")
    fun getIllustLiveData(@Query("list_type") type: String = "popular"): Observable<RankLiveResp>



    /**
     * 主页推荐列表
     * [category] 插画/漫画
     */
    @GET("/v1/{category}/recommended")
    fun getHomeRecommendData(@Category @Path("category")category: String = ILLUST, @Query("filter") filter: String = "for_android", @Query("include_ranking_illusts") ranking: Boolean = true,
                             @Query("include_privacy_policy") privacy: Boolean = true): Observable<IllustListResp>

    /**
     * 主页推荐列表
     * [category] 小说  神坑接口
     */
    @GET("/v1/{category}/recommended")
    fun getHomeNovelRecommendData(@Category @Path("category")category: String = NOVEL, @Query("filter") filter: String = "for_android", @Query("include_ranking_novels") ranking: Boolean = true,
                             @Query("include_privacy_policy") privacy: Boolean = true): Observable<NovelListResp>

    /**
     * 主页特辑
     */
    @GET("/v1/spotlight/articles")
    fun getIllustPixivisionData(@Query("filter") filter: String = "for_android", @Query("category") category: String): Observable<SpotLightResp>

    /**
     * 加载更多
     */
    @GET
    fun getMoreRecommend(@Url nextUrl: String): Observable<IllustListResp>

    /**
     * 加载更多小说
     */
    @GET
    fun getMoreNovelRecommend(@Url nextUrl: String): Observable<NovelListResp>

}