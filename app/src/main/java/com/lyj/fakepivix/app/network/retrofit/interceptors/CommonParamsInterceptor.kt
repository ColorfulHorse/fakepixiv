package com.lyj.fakepivix.app.network.retrofit.interceptors

import com.lyj.fakepivix.app.constant.Constant
import com.lyj.fakepivix.app.data.source.remote.UserRepository
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author greensun
 *
 * @date 2019/4/11
 *
 * @desc 公共参数拦截器
 */
class CommonParamsInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var oldReq = chain.request()
        val url = oldReq.url().toString()
        if (url.contains(Constant.Net.BASE_URL)) {
            val token = UserRepository.instance.accessToken
            if ("GET" == oldReq.method()) {
                oldReq = oldReq.newBuilder().url(oldReq.url()
                        .newBuilder()
                        .addQueryParameter("filter", "for_android")
                        .build()
                ).build()
            }
            token?.let {
                val newReq = oldReq
                        .newBuilder()
                        .addHeader("App-OS", "android")
                        .addHeader("Accept-Language", "zh_CN")
                        .addHeader(Constant.Net.HEADER_TOKEN, token)
                        .build()
                return chain.proceed(newReq)
            }
        }
        return chain.proceed(oldReq)
    }

}