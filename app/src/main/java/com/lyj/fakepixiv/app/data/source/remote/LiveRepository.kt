package com.lyj.fakepixiv.app.data.source.remote

import android.util.ArrayMap
import com.lyj.fakepixiv.app.data.model.response.Live
import com.lyj.fakepixiv.app.data.model.response.LiveListResp
import com.lyj.fakepixiv.app.network.retrofit.RetrofitManager
import com.lyj.fakepixiv.app.network.service.LiveService
import com.lyj.fakepixiv.app.reactivex.io
import io.reactivex.Observable

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc
 */
class LiveRepository private constructor() {

    private val service: LiveService by lazy { RetrofitManager.instance.liveService }
    
    companion object {
        val instance by lazy { LiveRepository() }
    }

    private val liveList: ArrayMap<String, Live> = ArrayMap()

    fun loadRecommend(): Observable<LiveListResp> {
        return service
                .getLiveListData()
                .map {
//                    it.lives.forEach { live -> liveList[live.id] = live }
                    it
                }
                .io()
    }

}