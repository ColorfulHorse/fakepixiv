package com.lyj.fakepivix.app.base;

import com.lyj.fakepivix.app.network.ApiService
import com.lyj.fakepivix.app.network.retrofit.RetrofitManager

/**
 * @author greensun
 * @date 2019/3/16
 * @desc
 */
open class BaseModel : IModel {
    protected val mApi: ApiService = RetrofitManager.instance.obtainService(ApiService::class.java)

    override fun destroy() {

    }
}
