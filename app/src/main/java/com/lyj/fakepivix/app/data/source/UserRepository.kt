package com.lyj.fakepivix.app.data.source

import com.google.gson.Gson
import com.lyj.fakepivix.app.constant.Constant
import com.lyj.fakepivix.app.data.model.response.LoginData
import com.lyj.fakepivix.app.data.model.response.LoginError
import com.lyj.fakepivix.app.data.model.response.LoginResp
import com.lyj.fakepivix.app.network.ApiException
import com.lyj.fakepivix.app.network.ApiService
import com.lyj.fakepivix.app.network.retrofit.RetrofitManager
import com.lyj.fakepivix.app.reactivex.retryWhenTokenInvalid
import com.lyj.fakepivix.app.utils.SPUtil
import io.reactivex.Observable
import retrofit2.HttpException

/**
 * @author greensun
 *
 * @date 2019/4/10
 *
 * @desc
 */
class UserRepository private constructor(){

    companion object {
        val instance: UserRepository by lazy { UserRepository() }
    }

    var loginData: LoginData? = null
    var accessToken: String? = null

    fun login(userName: String, password: String): Observable<LoginData> {
        return RetrofitManager.instance
                .obtainService(ApiService::class.java)
                .login(userName = userName, password = password)
                .map { it.response }
                .doOnNext {
                    accessToken = "${it.token_type} ${it.access_token}"
                    loginData = it
                    SPUtil.saveLoginData(it)
                }

    }

    /**
     * 用refreshToken登陆
     */
    fun reLogin(cache: LoginData): Observable<LoginData> {
        this.loginData = cache
        with(cache) {
            return RetrofitManager.instance
                    .obtainService(ApiService::class.java)
                    .login(grantType = Constant.Net.GRANT_TYPE_TOKEN, refreshToken = refresh_token, deviceToken = device_token)
                    .map { it.response }
                    .doOnNext {
                        accessToken = "${it.token_type} ${it.access_token}"
                        loginData = it
                        SPUtil.saveLoginData(it)
                    }
        }
    }
}