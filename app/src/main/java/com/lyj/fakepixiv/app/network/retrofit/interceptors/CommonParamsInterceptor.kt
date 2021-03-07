package com.lyj.fakepixiv.app.network.retrofit.interceptors

import android.text.TextUtils
import com.lyj.fakepixiv.app.constant.Constant
import com.lyj.fakepixiv.app.data.source.remote.UserRepository
import com.lyj.fakepixiv.app.utils.EncryptUtil
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author greensun
 *
 * @date 2019/4/11
 *
 * @desc 公共参数拦截器
 */
class CommonParamsInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var oldReq = chain.request()
        val url = oldReq.url().toString()
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
            oldReq = oldReq.newBuilder()
                    .url(urlBuilder.build())
                    .build()
        } else if ("POST" == oldReq.method()) {
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
                oldReq = oldReq.newBuilder().post(newBody.build()).build()
            }
        }

        // 加公共头部
        val timeStr = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.US).format(Date())
        val hash = EncryptUtil.encodeClientHash(timeStr + "28c1fdd170a5204386cb1313c7077b34f83e4aaf4aa829ce78c231e05b0bae2c")
        oldReq = oldReq
                .newBuilder()
                .addHeader("App-OS", "android")
                .addHeader("Accept-Language", "zh_CN")
                .addHeader("X-Client-Time", timeStr)
                .addHeader("X-Client-Hash", hash)
                .addHeader("User-Agent", "PixivAndroidApp/5.0.235 (Android 9; MIX 2)")
                .build()
        if (!url.contains(Constant.Net.AUTH_URL)) {
            var token: String? = null
            UserRepository.instance.loginData?.let {
//                token = "${it.token_type} ${it.access_token}"
                token = "Bearer ${it.access_token}"
            }
            if ("GET" == oldReq.method()) {
                val urlBuilder = oldReq.url().newBuilder()
                oldReq = oldReq.newBuilder().url(urlBuilder
                        .addQueryParameter("filter", "for_android")
                        .build()
                ).build()
            }
//            if (token == null) {
//                token = "Bearer l-f9qZ0ZyqSwRyZs8-MymbtWBbSxmCu1pmbOlyisou8"
//            }
            token?.let {
                val newReq = oldReq
                        .newBuilder()
                        .addHeader(Constant.Net.HEADER_TOKEN, it)
                        .build()
                return chain.proceed(newReq)
            }
        }
        return chain.proceed(oldReq)
    }

}