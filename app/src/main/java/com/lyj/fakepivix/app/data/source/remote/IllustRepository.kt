package com.lyj.fakepivix.app.data.source.remote

import android.util.ArrayMap
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.model.response.IllustListResp
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
class IllustRepository private constructor() {

    companion object {
        val instance by lazy { IllustRepository() }
    }

    var nextUrl = ""

    private val illustList: ArrayMap<String, Illust> = ArrayMap()

    fun loadRecommend(): Observable<IllustListResp> {
        return RetrofitManager.instance
                .apiService
                .getIllustRecommendData()
                .retryWhenTokenInvalid()
                .doOnNext {
                    with(it) {
                        nextUrl = next_url
                        illusts.forEach {
                            illust ->
                            illustList[illust.id.toString()] = illust
                        }
                        ranking_illusts.forEach {
                            illust ->
                            illustList[illust.id.toString()] = illust
                        }
                    }
                }
                .schedulerTransformer()
    }

    fun loadMore(): Observable<IllustListResp> {
        return RetrofitManager.instance
                .apiService
                .getMoreIllustRecommend(nextUrl)
                .retryWhenTokenInvalid()
                .doOnNext {
                    with(it) {
                        nextUrl = next_url
                        illusts.forEach {
                            illust ->
                            illustList[illust.id.toString()] = illust
                        }
                    }
                }
                .schedulerTransformer()
    }

}