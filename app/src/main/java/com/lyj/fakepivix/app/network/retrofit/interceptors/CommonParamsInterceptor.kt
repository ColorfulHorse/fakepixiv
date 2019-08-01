package com.lyj.fakepivix.app.network.retrofit.interceptors

import android.text.TextUtils
import com.lyj.fakepivix.app.constant.Constant
import com.lyj.fakepivix.app.data.source.remote.UserRepository
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Response
import java.nio.file.attribute.AclEntry.newBuilder

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
                val urlBuilder = oldReq.url().newBuilder()
                val size = oldReq.url().querySize()
                for (i in 0 until size) {
                    val key = oldReq.url().queryParameterName(i)
                    val param = oldReq.url().queryParameterValue(i)
                    if (TextUtils.isEmpty(param)) {
                        urlBuilder.removeAllQueryParameters(key)
                    }
                }
                oldReq = oldReq.newBuilder().url(urlBuilder
                        .addQueryParameter("filter", "for_android")
                        .build()
                ).build()
            }else if ("POST" == oldReq.method()) {
                val requestBody = oldReq.body()
                if (requestBody is FormBody) {
                    val newBody = FormBody.Builder()
                    for (i in 0 until requestBody.size()) {
                        val key = requestBody.encodedName(i)
                        val value = requestBody.encodedValue(i)
                        if (value.isNotEmpty()) {
                            newBody.addEncoded(key, value)
                        }
                    }
                    oldReq = oldReq.newBuilder().post(requestBody).build()
                }
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