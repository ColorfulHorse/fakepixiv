package com.lyj.fakepixiv.app.network.retrofit.interceptors

import com.lyj.fakepixiv.app.constant.Constant
import com.lyj.fakepixiv.app.data.model.response.LoginData
import com.lyj.fakepixiv.app.data.model.response.LoginError
import com.lyj.fakepixiv.app.data.source.remote.UserRepository
import com.lyj.fakepixiv.app.network.ApiException
import com.lyj.fakepixiv.app.utils.SPUtil
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
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

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        val response: Response
        try {
            response = chain.proceed(req)
        } catch (err: Exception) {
            val error = convertException(err)
            throw error
        }
        when (response.code()) {
            400 -> {
                when {
                    req.url().toString().contains(Constant.Net.AUTH_URL) -> {
                        // 登录失败
                        val errorBody = response.newBuilder().body(response.body()).build().body()?.string()
                        val adapter = moshi.adapter<LoginError>(LoginError::class.java)
                        if (errorBody != null) {
                            val loginError = adapter.fromJson(errorBody)
                            loginError?.let {
                                throw ApiException(it.errors.system.code)
                            }
                        }
                        throw ApiException()
                    }
//                    req.url().toString().contains(Constant.Net.ACCOUNT_URL) -> return response.newBuilder()
//                            .code(200)
//                            .build()
                    else -> {
                        // token过期同步刷新token
                        //throw ApiException(ApiException.CODE_TOKEN_INVALID)
                        var throwable: Throwable = ApiException()
                        var loginData =  UserRepository.instance.loginData
                        try {
                            // 如果别的请求已经刷新了token直接用就可以了
                            var cacheData: LoginData? = null
                            // 没有登陆过
                            if (loginData == null) {
                                cacheData = SPUtil.getLoginData()
                            }else if (loginData.needRefresh()) {
                                // token过期
                                cacheData = loginData
                            }
                            cacheData?.let {
                                // 刷新token
                                loginData = UserRepository.instance.refreshToken(it)
                            }
                            // 如果别的请求已经刷新了token直接用就可以了
                            loginData?.let {
                                val newReq = req.newBuilder()
                                        .header(Constant.Net.HEADER_TOKEN, "Bearer ${it.access_token}")
                                        .build()
                                return chain.proceed(newReq)
                            }
                        }catch (err: IOException) {
                            throwable = err
                        }
                        throw convertException(throwable)
                    }
                }
            }
            else -> return response
        }
    }

    private fun convertException(err: Throwable): Throwable {
        return when (err) {
            is SocketTimeoutException -> {
                ApiException(ApiException.CODE_TIMEOUT)
            }
            is ConnectException -> {
                ApiException(ApiException.CODE_CONNECT)
            }
            else -> err
        }
    }
}