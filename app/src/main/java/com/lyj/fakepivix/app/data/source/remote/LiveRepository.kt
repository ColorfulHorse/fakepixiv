package com.lyj.fakepivix.app.data.source.remote

import android.util.ArrayMap
import com.lyj.fakepivix.app.data.model.response.Live
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
class LiveRepository private constructor() {

    companion object {
        val instance by lazy { LiveRepository() }
    }

    private val liveList: ArrayMap<String, Live> = ArrayMap()
    private var nextUrl = ""

    fun loadRecommend(): Observable<List<Live>> {
        return RetrofitManager.instance
                .apiService
                .getIllustLiveData()
                .retryWhenTokenInvalid()
                .map { 
                    nextUrl = it.next_url
                    it.lives.forEach { live -> liveList[live.id] = live }
                    it.lives
                }
                .schedulerTransformer()
    }
}