package com.lyj.fakepivix.app.data.source.remote

import android.util.ArrayMap
import com.lyj.fakepivix.app.constant.COMIC
import com.lyj.fakepivix.app.constant.NOVEL
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.model.response.IllustListResp
import com.lyj.fakepivix.app.data.model.response.NovelListResp
import com.lyj.fakepivix.app.network.retrofit.RetrofitManager
import com.lyj.fakepivix.app.reactivex.retryWhenTokenInvalid
import com.lyj.fakepivix.app.reactivex.schedulerTransformer
import io.reactivex.Observable

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc
 */
class NovelRepository private constructor() {

    companion object {
        val instance by lazy { NovelRepository() }
    }

    var nextUrl = ""

    private val illustList: ArrayMap<String, Illust> = ArrayMap()

    fun loadRecommend(): Observable<NovelListResp> {
        return RetrofitManager.instance
                .apiService
                .getHomeNovelRecommendData()
                .retryWhenTokenInvalid()
                .doOnNext {
                    with(it) {
                        nextUrl = next_url
                        novels.forEach {
                            illust ->
                            illustList[illust.id.toString()] = illust
                        }
                        ranking_novels.forEach {
                            illust ->
                            illustList[illust.id.toString()] = illust
                        }
                    }
                }
                .schedulerTransformer()
    }

    fun loadMore(): Observable<NovelListResp> {
        return RetrofitManager.instance
                .apiService
                .getMoreNovelRecommend(nextUrl)
                .retryWhenTokenInvalid()
                .doOnNext {
                    with(it) {
                        nextUrl = next_url
                        novels.forEach {
                            illust ->
                            illustList[illust.id.toString()] = illust
                        }
                    }
                }
                .schedulerTransformer()
    }

}