package com.lyj.fakepixiv.app.network.service

import com.lyj.fakepixiv.app.constant.Constant
import com.lyj.fakepixiv.app.data.model.response.*
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
interface IllustExtService {

    /**
     * 漫画系列
     */
    @GET("/v1/illust/series")
    suspend fun getMangaSeries(@Query("illust_series_id") seriesId: String): SeriesExt

    /**
     * 小说系列
     */
    @GET("/v2/novel/series")
    suspend fun getNovelSeries(@Query("series_id") seriesId: String): NovelSeries

    /**
     * 获取是否有上一章下一章
     */
    @GET("/v1/illust-series/illust")
    suspend fun getSeriesContext(@Query("illust_id") illustId: String): SeriesContextResp


    /**
     * 获取作品评论
     */
    @GET("/v2/illust/comments")
    fun getIllustComment(@Query("illust_id") illustId: String): Observable<CommentListResp>

    @GET
    suspend fun getMoreComment(@Url nextUrl: String): CommentListResp


    /**
     * 获取评论回复
     */
    @GET("/v1/illust/comment/replies")
    suspend fun getApplies(@Query("comment_id") id: Long): CommentListResp

    /**
     * 添加评论
     */
    @POST("/v1/illust/comment/add")
    @FormUrlEncoded
    suspend fun addComment(@Field("illust_id") illustId: Long,
                           @Field("comment") comment: String,
                           @Field("parent_comment_id") parentId: String = ""): Any

    /**
     * 删除评论
     */
    @POST("/v1/illust/comment/delete")
    @FormUrlEncoded
    suspend fun deleteComment(@Field("comment_id") parentId: Long): Any
}