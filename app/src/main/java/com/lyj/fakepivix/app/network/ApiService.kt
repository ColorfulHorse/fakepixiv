package com.lyj.fakepivix.app.network


import com.lyj.fakepivix.app.constant.IllustCategory.*
import com.lyj.fakepivix.app.constant.Constant
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.constant.Restrict
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
    fun getRecommendIllust(@IllustCategory @Path("category")category: String = ILLUST, @Query("include_ranking_illusts") ranking: Boolean = true,
                           @Query("include_privacy_policy") privacy: Boolean = true): Observable<IllustListResp>

    /**
     * 主页推荐列表
     * [category] 小说  神坑接口
     */
    @GET("/v1/{category}/recommended")
    fun getHomeNovelRecommendData(@IllustCategory @Path("category")category: String = NOVEL, @Query("include_ranking_novels") ranking: Boolean = true,
                                  @Query("include_privacy_policy") privacy: Boolean = true): Observable<NovelListResp>

    /**
     * 主页特辑
     */
    @GET("/v1/spotlight/articles")
    fun getIllustPixivisionData(@Query("category") category: String): Observable<SpotLightResp>

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



    /**·······
     * 最新-推荐用户
     */
    @GET("/v1/user/recommended")
    fun getUserRecommend(): Observable<UserPreviewListResp>

    /**
     * 最新-关注者
     * [category] 插画/漫画
     * [filter] 筛选条件 全部/公开/私密
     */
    @GET("/v2/{category}/follow")
    fun getFollowIllustData(@IllustCategory @Path("category")category: String = ILLUST, @Restrict @Query("restrict") restrict: String = Restrict.ALL): Observable<IllustListResp>

    /**
     * 最新-关注者
     * [category] 小说
     */
    @GET("/v1/{category}/follow")
    fun getFollowNovelData(@IllustCategory @Path("category")category: String = NOVEL, @Restrict @Query("restrict") restrict: String = Restrict.ALL): Observable<NovelListResp>



    /**
     * 最新-最新
     * [category] 插画/漫画
     */
    @GET("/v1/illust/new")
    fun getNewIllustData(@IllustCategory @Query("content_type")category: String): Observable<IllustListResp>

    /**
     * 最新-最新
     * [category] 小说
     */
    @GET("/v1/novel/new")
    fun getNewNovelData(): Observable<NovelListResp>

    /**
     * 最新-好P友
     * 插画漫画
     */
    @GET("/v2/illust/mypixiv")
    fun getFriendIllustData(): Observable<IllustListResp>

    /**
     * 最新-好P友
     * 小说
     */
    @GET("/v1/illust/mypixiv")
    fun getFriendNovelData(): Observable<NovelListResp>


    /**
     * 获取热门搜索标签
     * [category] 插画漫画/小说
     */
    @GET("/v1/trending-tags/{category}")
    fun getIllustSearchTag(@IllustCategory @Path("category")category: String): Observable<SearchTagListResp>


    /**
     * 获取用户作品列表
     * [userId] 用户id
     */
    @GET("/v1/user/illusts")
    fun getUserIllustData(@Query("user_id")userId: String, @IllustCategory @Query("type")category: String = ILLUST): Observable<IllustListResp>


    /**
     * 获取相关作品
     */
    @GET("/v2/illust/related")
    fun getRelatedIllustData(@Query("illust_id")illustId: String): Observable<IllustListResp>

    /**
     * 获取作品评论
     */
    @GET("/v2/illust/comments")
    fun getIllustComment(@Query("illust_id")illustId: String): Observable<CommentListResp>

    @GET
    fun getMoreComment(@Url nextUrl: String): Observable<CommentListResp>

    /**
     * 收藏
     */
    @POST("/v2/illust/bookmark/add")
    @FormUrlEncoded
    fun starIllust(@Field("illust_id")illustId: String, @Restrict @Field("restrict") restrict: String = Restrict.PUBLIC): Observable<Any>

    /**
     * 取消收藏
     */
    @POST("/v2/illust/bookmark/delete")
    @FormUrlEncoded
    fun unStarIllust(@Field("illust_id")illustId: String): Observable<Any>
}
