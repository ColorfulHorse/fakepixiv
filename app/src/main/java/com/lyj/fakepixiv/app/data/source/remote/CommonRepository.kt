package com.lyj.fakepixiv.app.data.source.remote

import android.util.ArrayMap
import com.lyj.fakepixiv.app.data.model.response.Live
import com.lyj.fakepixiv.app.data.model.response.LiveListResp
import com.lyj.fakepixiv.app.network.retrofit.RetrofitManager
import com.lyj.fakepixiv.app.network.service.CommonService
import com.lyj.fakepixiv.app.network.service.IllustService
import com.lyj.fakepixiv.app.network.service.LiveService
import com.lyj.fakepixiv.app.reactivex.schedulerTransform
import io.reactivex.Observable

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc
 */
class CommonRepository private constructor() {

    val service: CommonService by lazy { RetrofitManager.instance.commonService }
    
    companion object {
        val instance by lazy { CommonRepository() }
    }
}