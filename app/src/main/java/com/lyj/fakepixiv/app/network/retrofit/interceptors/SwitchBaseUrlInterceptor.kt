package com.lyj.fakepixiv.app.network.retrofit.interceptors

import com.lyj.fakepixiv.app.constant.Constant
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @author greensun
 *
 * @date 2019/4/11
 *
 * @desc 用于切换baseUrl的拦截器
 */
class SwitchBaseUrlInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
            val oldReq = chain.request()
            val headers = oldReq.headers().toMultimap()
            val header = headers[Constant.Net.SWITCH_HEADER]
            if (header!= null) {
                val urlStr = when (header[0]) {
                    Constant.Net.TAG_AUTH -> Constant.Net.AUTH_URL
                    else -> Constant.Net.BASE_URL
                }
                val url = HttpUrl.parse(urlStr)
                if (url != null) {
                    val newUrl = oldReq.url()
                            .newBuilder()
                            .host(url.host())
                            .build()
                    val newReq = oldReq.newBuilder()
                            .removeHeader(Constant.Net.SWITCH_HEADER)
                            .url(newUrl)
                            .build()
                    return chain.proceed(newReq)
                }
            }
        return chain.proceed(oldReq)
    }
}