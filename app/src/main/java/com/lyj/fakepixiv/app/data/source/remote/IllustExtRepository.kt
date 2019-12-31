package com.lyj.fakepixiv.app.data.source.remote

import com.lyj.fakepixiv.app.data.model.response.*
import com.lyj.fakepixiv.app.network.retrofit.RetrofitManager
import com.lyj.fakepixiv.app.network.service.IllustExtService
import com.lyj.fakepixiv.app.reactivex.io
import io.reactivex.Observable

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc
 */
class IllustExtRepository private constructor() {

    val service: IllustExtService by lazy { RetrofitManager.instance.illustExtService }
    
    companion object {
        val instance by lazy { IllustExtRepository() }
    }

    suspend fun getSeriesContext(illustId: String): SeriesContextResp {
        return service
                .getSeriesContext(illustId)
    }

    suspend fun getSeriesDetail(seriesId: String): SeriesExt {
        return service
                .getMangaSeries(seriesId)
    }

    suspend fun getNovelSeriesDetail(seriesId: String): NovelSeries {
        return service
                .getNovelSeries(seriesId)
    }

    /**
     * 获取作品评论
     */
    fun loadIllustComment(illustId: String): Observable<CommentListResp> {
        return service
                .getIllustComment(illustId)
                .io()
    }

    suspend fun loadMoreComment(nextUrl: String): CommentListResp {
        return service
                .getMoreComment(nextUrl)
    }

}