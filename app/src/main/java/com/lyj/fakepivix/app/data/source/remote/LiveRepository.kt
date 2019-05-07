package com.lyj.fakepivix.app.data.source.remote

import android.util.ArrayMap
import com.lyj.fakepivix.app.data.model.response.Live
import com.lyj.fakepivix.app.data.model.response.LiveListResp
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
class LiveRepository private constructor() {

    companion object {
        val instance by lazy { LiveRepository() }
    }

    private val liveList: ArrayMap<String, Live> = ArrayMap()

    fun loadRecommend(): Observable<LiveListResp> {
        return RetrofitManager.instance
                .apiService
                .getLiveListData()
                .map {
//                    it.lives.forEach { live -> liveList[live.id] = live }
                    it
                }
                .schedulerTransform()
    }

}