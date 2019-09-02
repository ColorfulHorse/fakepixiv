package com.lyj.fakepivix.app.network


import com.lyj.fakepivix.app.constant.IllustCategory.*
import com.lyj.fakepivix.app.constant.Constant
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.constant.Restrict
import com.lyj.fakepivix.app.data.model.response.*
import com.lyj.fakepivix.app.network.retrofit.interceptors.SwitchBaseUrlInterceptor
import io.reactivex.CompletableOnSubscribe
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
    fun getRecommendIllust(@IllustCategory @Path("category")category: String = ILLUST,
                           @Query("include_ranking_illusts") ranking: Boolean = true,
                           @Query("include_ranking_novels") ranking2: Boolean = true,
                           @Query("include_privacy_policy") privacy: Boolean = true): Observable<IllustListResp>

    /**
     * 获取排行榜
     */
    @GET("/v1/{category}/ranking")
    fun getRankIllust(@Path("category") category: String = ILLUST, @Query("mode") mode: String, @Query("date") date: String): Observable<IllustListResp>


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



    /**·······
     * 最新-推荐用户
     */
    @GET("/v1/user/recommended")
    suspend fun getUserRecommend(): UserPreviewListResp


    /**
     * 粉丝
     */
    @GET("/v1/user/follower")
    suspend fun getFollower(@Query("user_id") userId: String): UserPreviewListResp

    /**
     * 好P友
     */
    @GET("/v1/user/mypixiv")
    suspend fun getMypixiv(@Query("user_id") userId: String): UserPreviewListResp

    /**
     * 关注的人
     */
    @GET("/v1/user/following")
    suspend fun getFollowing(@Query("user_id") userId: String, @Query("restrict")restrict: String): UserPreviewListResp

    /**
     * 更多用户
     */
    @GET
    suspend fun getMoreUser(@Url nextUrl: String): UserPreviewListResp

    /**
     * 获取用户作品列表
     * [userId] 用户id
     */
    @GET("/v1/user/illusts")
    suspend fun getUserIllustData(@Query("user_id")userId: String, @IllustCategory @Query("type")category: String = ILLUST): IllustListResp

    /**
     * 获取用户小说作品列表
     * [userId] 用户id
     */
    @GET("/v1/user/novels")
    suspend fun getUserNovels(@Query("user_id")userId: String): IllustListResp

    /**
     * 获取用户详情
     */
    @GET("/v1/user/detail")
    suspend fun getUserDetail(@Query("user_id")userId: String): UserInfo

    /**
     * 获取用户收藏
     * [userId] 用户id
     * [category] 插画漫画illust/小说novel
     *
     */
    @GET("/v1/user/bookmarks/{category}")
    suspend fun getUserBookmarks(@IllustCategory @Path("category")category: String, @Query("user_id")userId: String,
                         @Restrict @Query("restrict")restrict: String = Restrict.PUBLIC): IllustListResp

    /**
     * 获取相关用户
     */
    @GET("/v1/user/related")
    fun getRelatedUsers(@Query("seed_user_id")userId: String): Observable<UserPreviewListResp>

    /**
     * 关注
     */
    @POST("/v1/user/follow/add")
    @FormUrlEncoded
    fun followUser(@Field("user_id")userId: String, @Restrict @Field("restrict") restrict: String = Restrict.PUBLIC): Observable<Any>

    @POST("/v1/user/follow/delete")
    @FormUrlEncoded
    fun unFollowUser(@Field("user_id")userId: String): Observable<Any>




    /**
     * 最新-关注者
     * [category] 插画/漫画
     * [filter] 筛选条件 全部/公开/私密
     */
    @GET("/v2/{category}/follow")
    fun getFollowIllustData(@IllustCategory @Path("category")category: String = ILLUST,
                            @Restrict @Query("restrict") restrict: String = Restrict.ALL): Observable<IllustListResp>

    /**
     * 最新-关注者
     * [category] 小说
     */
    @GET("/v1/{category}/follow")
    fun getFollowNovelData(@IllustCategory @Path("category")category: String = NOVEL,
                           @Restrict @Query("restrict") restrict: String = Restrict.ALL): Observable<IllustListResp>



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
    fun getNewNovelData(): Observable<IllustListResp>

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
    fun getFriendNovelData(): Observable<IllustListResp>


    /**
     * 获取热门搜索标签
     * [category] 插画漫画/小说
     */
    @GET("/v1/trending-tags/{category}")
    fun getIllustSearchTag(@IllustCategory @Path("category")category: String): Observable<SearchTagListResp>



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
    @POST("/v1/illust/bookmark/delete")
    @FormUrlEncoded
    fun unStarIllust(@Field("illust_id")illustId: String): Observable<Any>

    /**
     * 收藏
     */
    @POST("/v2/novel/bookmark/add")
    @FormUrlEncoded
    fun starNovel(@Field("novel_id")illustId: String, @Restrict @Field("restrict") restrict: String = Restrict.PUBLIC): Observable<Any>

    /**
     * 收藏
     */
    @POST("/v2/novel/bookmark/delete")
    @FormUrlEncoded
    fun unStarNovel(@Field("novel_id")illustId: String): Observable<Any>


    /**
     * 自动完成搜索关键字
     */
    @GET("/v2/search/autocomplete")
    fun searchAutoComplete(@Query("word")keyword: String): Observable<TagListResp>


    /**
     * 按热度搜索
     */
    @GET("/v1/search/popular-preview/{category}")
    fun searchPopularIllust(@IllustCategory @Path("category")category: String,
                            @Query("word")keyword: String,
                            @Query("search_target")mode: String = Constant.Request.KEY_SEARCH_PARTIAL,
                            @Query("start_date")start_date: String = "",
                            @Query("end_date")end_date: String = "",
                            @Query("include_translated_tag_results")translate: Boolean = true): Observable<IllustListResp>

    /**
     * 按顺序搜索
     */
    @GET("/v1/search/{category}")
    fun searchIllust(@IllustCategory @Path("category")category: String,
                     @Query("word")keyword: String,
                     @Query("sort")sort: String,
                     @Query("search_target")strategy: String = Constant.Request.KEY_SEARCH_PARTIAL,
                     @Query("start_date")start_date: String = "",
                     @Query("end_date")end_date: String = "",
                     @Query("include_translated_tag_results")translate: Boolean = true): Observable<IllustListResp>

    /**
     * 搜索用户
     */
    @GET("/v1/search/user")
    suspend fun searchUser(@Query("word")keyword: String): UserPreviewListResp

    /**
     * 获取小说详情
     */
    @GET("/v1/novel/text")
    suspend fun getNovelText(@Query("novel_id")novelId: String): NovelText

    /**
     * 添加书签
     */
    @POST("/v1/novel/marker/add")
    @FormUrlEncoded
    suspend fun markNovel(@Field("novel_id")novelId: String, @Field("page")page: Int): Any

    /**
     * 删除书签
     */
    @POST("/v1/novel/marker/delete")
    @FormUrlEncoded
    suspend fun unMarkNovel(@Field("novel_id")novelId: String): Any
}
