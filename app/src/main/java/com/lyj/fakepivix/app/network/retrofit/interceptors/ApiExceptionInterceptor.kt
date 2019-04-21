package com.lyj.fakepivix.app.network.retrofit.interceptors

import com.lyj.fakepivix.app.constant.Constant
import com.lyj.fakepivix.app.data.model.response.LoginError
import com.lyj.fakepivix.app.network.ApiException
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.Response
import java.net.ConnectException
import java.net.SocketTimeoutException

/**
 * @author greensun
 *
 * @date 2019/4/12
 *
 * @desc httpException拦截器
 */
class ApiExceptionInterceptor : Interceptor {

private val moshi: Moshi by lazy { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() }

    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        try {
            chain.proceed(req)
        }catch (err: Exception) {
            when (err) {
                is SocketTimeoutException -> {
                    throw ApiException(ApiException.CODE_TIMEOUT)
                }
                is ConnectException -> {
                    throw ApiException(ApiException.CODE_CONNECT)
                }
                else -> throw err
            }
        }
        val response = chain.proceed(req)
        when(response.code()) {
            400 -> {
                if (req.url().toString().contains(Constant.Net.AUTH_URL)) {
                    val errorBody = response.body()?.string()
                    val adapter = moshi.adapter<LoginError>(LoginError::class.java)
                    if (errorBody != null) {
                        val loginError = adapter.fromJson(errorBody)
                        loginError?.let {
                            throw ApiException(it.errors.system.code)
                        }
                    }
                    throw ApiException()
                }else {
                    throw ApiException(ApiException.CODE_TOKEN_INVALID)
                }
            }
            else -> return chain.proceed(req)
        }
    }
}