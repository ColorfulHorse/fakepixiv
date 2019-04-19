package com.lyj.fakepivix.app.network.retrofit.interceptors

import com.google.gson.Gson
import com.lyj.fakepivix.app.constant.Constant
import com.lyj.fakepivix.app.data.model.response.LoginError
import com.lyj.fakepivix.app.network.ApiException
import okhttp3.Interceptor
import okhttp3.Response
import java.lang.Exception
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
        val code = response.code()
        when(code) {
            400 -> {
                if (req.url().toString().contains(Constant.Net.AUTH_URL)) {
                    val errorBody = response.body()?.string()
                    val gson = Gson()
                    val loginError = gson.fromJson<LoginError>(errorBody, LoginError::class.java)
                    throw ApiException(loginError.errors.system.code)
                }else {
                    throw ApiException(ApiException.CODE_TOKEN_INVALID)
                }
            }
            else -> return chain.proceed(req)
        }
    }
}