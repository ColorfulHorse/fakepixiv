package com.lyj.fakepixiv.app.network.service

import com.lyj.fakepixiv.app.constant.Constant
import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.constant.Restrict
import com.lyj.fakepixiv.app.data.model.response.*
import io.reactivex.Observable
import retrofit2.http.*

/**
 * @author green sun
 *
 * @date 2019/10/16
 *
 * @desc
 */
interface IllustService {
    /**
     * 登录页滚动图片墙
     */
    @GET("/v1/walkthrough/illusts")
    fun getWallPaperData(): Observable<IllustListResp>

    /**
     * 主页推荐列表
     * [category] 插画/漫画
     */
    @GET("/v1/{category}/recommended")
    fun getRecommendIllust(@IllustCategory @Path("category")category: String = IllustCategory.ILLUST,
                           @Query("include_ranking_illusts") ranking: Boolean = true,
                           @Query("include_ranking_novels") ranking2: Boolean = true,
                           @Query("include_privacy_policy") privacy: Boolean = true): Observable<IllustListResp>

    /**
     * 获取排行榜
     */
    @GET("/v1/{category}/ranking")
    suspend fun getRankIllust(@Path("category") category: String = IllustCategory.ILLUST,
                              @Query("mode") mode: String,
                              @Query("date") date: String): IllustListResp

    /**
     * 最新-关注者
     * [category] 插画/漫画
     * [filter] 筛选条件 全部/公开/私密
     */
    @GET("/v2/{category}/follow")
    suspend fun getFollowIllustData(@IllustCategory @Path("category")category: String = IllustCategory.ILLUST,
                                    @Restrict @Query("restrict") restrict: String = Restrict.ALL): IllustListResp

    /**
     * 最新-关注者
     * [category] 小说
     */
    @GET("/v1/{category}/follow")
    suspend fun getFollowNovelData(@IllustCategory @Path("category")category: String = IllustCategory.NOVEL,
                                   @Restrict @Query("restrict") restrict: String = Restrict.ALL): IllustListResp

    /**
     * 最新-最新
     * [category] 插画/漫画
     */
    @GET("/v1/illust/new")
    suspend fun getNewIllustData(@IllustCategory @Query("content_type")category: String): IllustListResp

    /**
     * 最新-最新
     * [category] 小说
     */
    @GET("/v1/novel/new")
    suspend fun getNewNovelData(): IllustListResp

    /**
     * 最新-好P友
     * 插画漫画
     */
    @GET("/v2/illust/mypixiv")
    suspend fun getFriendIllustData(): IllustListResp

    /**
     * 最新-好P友
     * 小说
     */
    @GET("/v1/illust/mypixiv")
    suspend fun getFriendNovelData(): IllustListResp

    /**
     * 获取用户作品列表
     * [userId] 用户id
     */
    @GET("/v1/user/illusts")
    suspend fun getUserIllustData(@Query("user_id")userId: String,
                                  @IllustCategory @Query("type")category: String = IllustCategory.ILLUST): IllustListResp

    /**
     * 获取用户小说作品列表
     * [userId] 用户id
     */
    @GET("/v1/user/novels")
    suspend fun getUserNovels(@Query("user_id")userId: String): IllustListResp

    /**
     * 获取用户收藏
     * [userId] 用户id
     * [category] 插画漫画illust/小说novel
     *
     */
    @GET("/v1/user/bookmarks/{category}")
    suspend fun getUserBookmarks(@IllustCategory @Path("category")category: String,
                                 @Query("user_id")userId: String,
                                 @Restrict @Query("restrict")restrict: String = Restrict.PUBLIC,
                                 @Query("tag")tag: String = ""): IllustListResp

    /**
     * 获取相关作品
     */
    @GET("/v2/illust/related")
    fun getRelatedIllustData(@Query("illust_id")illustId: String): Observable<IllustListResp>

    /**
     * 按热度搜索
     */
    @GET("/v1/search/popular-preview/{category}")
    suspend fun searchPopularIllust(@IllustCategory @Path("category")category: String,
                                    @Query("word")keyword: String,
                                    @Query("search_target")mode: String = Constant.Request.KEY_SEARCH_PARTIAL,
                                    @Query("start_date")start_date: String = "",
                                    @Query("end_date")end_date: String = "",
                                    @Query("include_translated_tag_results")translate: Boolean = true): IllustListResp

    /**
     * 按顺序搜索
     */
    @GET("/v1/search/{category}")
    suspend fun searchIllust(@IllustCategory @Path("category")category: String,
                             @Query("word")keyword: String,
                             @Query("sort")sort: String,
                             @Query("search_target")strategy: String = Constant.Request.KEY_SEARCH_PARTIAL,
                             @Query("start_date")start_date: String = "",
                             @Query("end_date")end_date: String = "",
                             @Query("include_translated_tag_results")translate: Boolean = true): IllustListResp

    /**
     * 获取作品评论
     */
    @GET("/v2/illust/comments")
    fun getIllustComment(@Query("illust_id")illustId: String): Observable<CommentListResp>

    @GET
    fun getMoreComment(@Url nextUrl: String): Observable<CommentListResp>

    /**
     * 获取插画详情
     */
    @GET("/v1/illust/detail")
    suspend fun getIllustDetail(@Query("illust_id")illustId: String): IllustResp


    /**
     * 获取收藏详情
     */
    @GET("/v2/illust/bookmark/detail")
    suspend fun getIllustBookmark(@Query("illust_id")illustId: String): BookMarkResp

    @GET("/v2/novel/bookmark/detail")
    suspend fun getNovelBookmark(@Query("novel_id")illustId: String): BookMarkResp

    /**
     * 收藏分类
     */
    @GET("/v1/user/bookmark-tags/{category}")
    suspend fun getBookmarkTag(@IllustCategory @Path("category")category: String,
                               @Query("user_id")userId: String,
                               @Restrict @Query("restrict") restrict: String = Restrict.PUBLIC): BookmarkTags

    /**
     * 收藏  illust_id=76767615&restrict=public&tags%5B%5D=fgo&tags%5B%5D=Fate%2FGrandOrder
     */
    @POST("/v2/illust/bookmark/add")
    @FormUrlEncoded
    fun starIllust(@Field("illust_id")illustId: String,
                   @FieldMap tags: Map<String, String> = mapOf(),
                   @Restrict @Field("restrict") restrict: String = Restrict.PUBLIC): Observable<Any>

    /**
     * 取消收藏
     */
    @POST("/v1/illust/bookmark/delete")
    @FormUrlEncoded
    fun unStarIllust(@Field("illust_id")illustId: String): Observable<Any>

    /**
     * 收藏小说
     */
    @POST("/v2/novel/bookmark/add")
    @FormUrlEncoded
    fun starNovel(@Field("novel_id")illustId: String,
                  @FieldMap tags: Map<String, String> = mapOf(),
                  @Restrict @Field("restrict") restrict: String = Restrict.PUBLIC): Observable<Any>

    /**
     * 收藏
     */
    @POST("/v2/novel/bookmark/delete")
    @FormUrlEncoded
    fun unStarNovel(@Field("novel_id")illustId: String): Observable<Any>

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


    /**
     * 漫画系列
     */
    @GET("/v1/illust/series")
    suspend fun getMangaSeries(@Query("illust_series_id")seriesId: String): SeriesExt

    /**
     * 小说系列
     */
    @GET("/v2/novel/series")
    suspend fun getNovelSeries(@Query("series_id")seriesId: String): NovelSeries

    /**
     * 获取是否有上一章下一章
     */
    @GET("/v1/illust-series/illust")
    suspend fun getSeriesContext(@Query("illust_id")illustId: String): SeriesContextResp

    /**
     * 加载更多
     */
    @GET
    fun getMoreIllust(@Url nextUrl: String): Observable<IllustListResp>

    // https://app-api.pixiv.net/v1/emoji HTTP/1.1
    // https://app-api.pixiv.net/v1/illust/comment/delete  comment_id=93773576
    /**
     * POST https://app-api.pixiv.net/v1/illust/comment/add HTTP/1.1  illust_id=77619699&comment=%28star%29  parent_comment_id=93741538
     */
    // https://app-api.pixiv.net/v1/illust/comment/replies?comment_id=93741538   commentlistresp
}