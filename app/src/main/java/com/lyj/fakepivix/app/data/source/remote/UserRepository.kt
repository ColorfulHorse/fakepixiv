package com.lyj.fakepivix.app.data.source.remote

import com.lyj.fakepivix.app.constant.Constant
import com.lyj.fakepivix.app.data.model.response.LoginData
import com.lyj.fakepivix.app.data.model.response.UserPreviewListResp
import com.lyj.fakepivix.app.network.retrofit.RetrofitManager
import com.lyj.fakepivix.app.reactivex.schedulerTransform
import com.lyj.fakepivix.app.utils.SPUtil
import io.reactivex.Observable

/**
 * @author greensun
 *
 * @date 2019/4/10
 *
 * @desc
 */
class UserRepository private constructor(){

    companion object {
        @JvmStatic
        val instance: UserRepository by lazy { UserRepository() }
    }

    var loginData: LoginData? = null
    var accessToken: String? = null

    fun login(userName: String, password: String): Observable<LoginData> {
        return RetrofitManager.instance
                .apiService
                .login(userName = userName, password = password)
                .map { it.response }
                .doOnNext {
                    accessToken = "Bearer ${it.access_token}"
                    loginData = it
                    SPUtil.saveLoginData(it)
                }
                .schedulerTransform()

    }

    /**
     * 用refreshToken登陆
     */
    fun reLogin(cache: LoginData): Observable<LoginData> {
        this.loginData = cache
        with(cache) {
            return RetrofitManager.instance
                    .apiService
                    .login(grantType = Constant.Net.GRANT_TYPE_TOKEN, refreshToken = refresh_token, deviceToken = device_token)
                    .map { it.response }
                    .doOnNext {
                        accessToken = "Bearer ${it.access_token}"
                        loginData = it
                        SPUtil.saveLoginData(it)
                    }
        }
    }

    fun getRecommendUsers(): Observable<UserPreviewListResp> {
        return RetrofitManager.instance
                .apiService
                .getUserRecommend()
                .schedulerTransform()
    }
}