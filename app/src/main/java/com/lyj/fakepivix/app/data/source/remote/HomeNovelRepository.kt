package com.lyj.fakepivix.app.data.source.remote

import android.util.ArrayMap
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.model.response.NovelListResp
import com.lyj.fakepivix.app.network.retrofit.RetrofitManager
import com.lyj.fakepivix.app.reactivex.schedulerTransform
import io.reactivex.Observable

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc
 */
class HomeNovelRepository private constructor() {

    companion object {
        val instance by lazy { HomeNovelRepository() }
    }

    var nextUrl = ""

    private val illustList: ArrayMap<String, Illust> = ArrayMap()

    fun loadRecommend(): Observable<NovelListResp> {
        return RetrofitManager.instance
                .apiService
                .getHomeNovelRecommendData()
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
                .schedulerTransform()
    }

    fun loadMore(): Observable<NovelListResp> {
        return RetrofitManager.instance
                .apiService
                .getMoreNovelRecommend(nextUrl)
                .doOnNext {
                    with(it) {
                        nextUrl = next_url
                        novels.forEach {
                            illust ->
                            illustList[illust.id.toString()] = illust
                        }
                    }
                }
                .schedulerTransform()
    }

    fun clear() {
        illustList.clear()
    }
}