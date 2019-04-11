package com.lyj.fakepivix.app.network.retrofit

import com.lyj.fakepivix.app.constant.Constant
import com.lyj.fakepivix.app.data.source.UserRepository
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
        val oldReq = chain.request()
        val url = oldReq.url().toString()
        if (Constant.Net.BASE_URL == url) {
            val token = UserRepository.instance.accessToken
            token?.let {
                val newReq =  oldReq.newBuilder()
                        .addHeader(Constant.Net.HEADER_TOKEN, token)
                        .build()
                return chain.proceed(newReq)
            }
        }
        return chain.proceed(oldReq)
    }

}