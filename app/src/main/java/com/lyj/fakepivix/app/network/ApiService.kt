package com.lyj.fakepivix.app.network


import com.lyj.fakepivix.app.constant.IllustCategory
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
     * 直播列表  [type] following:关注的直播
     */
    @GET("/v1/live/list")
    fun getLiveListData(@Query("list_type") type: String = "popular"): Observable<LiveListResp>



    /**
     * 主页推荐列表
     * [category] 插画/漫画
     */
    @GET("/v1/{category}/recommended")
    fun getRecommendIllust(@IllustCategory @Path("category")category: String = ILLUST, @Query("filter") filter: String = "for_android", @Query("include_ranking_illusts") ranking: Boolean = true,
                           @Query("include_privacy_policy") privacy: Boolean = true): Observable<IllustListResp>

    /**
     * 主页推荐列表
     * [category] 小说  神坑接口
     */
    @GET("/v1/{category}/recommended")
    fun getHomeNovelRecommendData(@IllustCategory @Path("category")category: String = NOVEL, @Query("filter") filter: String = "for_android", @Query("include_ranking_novels") ranking: Boolean = true,
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
    fun getMoreIllust(@Url nextUrl: String): Observable<IllustListResp>

    /**
     * 加载更多小说
     */
    @GET
    fun getMoreNovel(@Url nextUrl: String): Observable<NovelListResp>



    /**
     * 最新-推荐用户
     */
    @GET("/v1/user/recommended")
    fun getUserRecommend(@Query("filter") filter: String = "for_android"): Observable<UserPreviewListResp>

    /**
     * 最新-关注者
     * [category] 插画/漫画
     * [filter] 筛选条件 全部/公开/私密
     */
    @GET("/v2/{category}/follow")
    fun getFollowIllustData(@IllustCategory @Path("category")category: String = ILLUST, @Query("restrict") restrict: String = "all"): Observable<IllustListResp>

    /**
     * 最新-关注者
     * [category] 小说
     */
    @GET("/v1/{category}/follow")
    fun getFollowNovelData(@IllustCategory @Path("category")category: String = NOVEL, @Query("restrict") restrict: String = "all"): Observable<NovelListResp>

    // https://app-api.pixiv.net/v1/illust/new?filter=for_android&content_type=illust   最新-插画
    // https://app-api.pixiv.net/v1/illust/new?filter=for_android&content_type=manga   最新-漫画
    // https://app-api.pixiv.net/v1/novel/new 最新-小说
}