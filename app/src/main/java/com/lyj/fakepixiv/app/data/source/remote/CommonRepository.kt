package com.lyj.fakepixiv.app.data.source.remote

import com.lyj.fakepixiv.app.network.retrofit.RetrofitManager
import com.lyj.fakepixiv.app.network.service.CommonService

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