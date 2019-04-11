package com.lyj.fakepivix.app.data.source

import com.lyj.fakepivix.app.data.model.response.LoginData
import com.lyj.fakepivix.app.network.ApiException
import com.lyj.fakepivix.app.network.ApiService
import com.lyj.fakepivix.app.network.retrofit.RetrofitManager
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
        val instance: UserRepository by lazy { UserRepository() }
    }

    var loginData: LoginData? = null
    var accessToken: String? = null

    fun login(userName: String, password: String): Observable<LoginData> {
        return RetrofitManager.instance
                .obtainService(ApiService::class.java)
                .login()
                .map {
                    if (it.has_error) {
                        throw ApiException()
                    }
                    it.response
                }
                .doOnNext {
                    loginData = it
                    SPUtil.saveLoginData(it)
                }

    }

    fun reLogin(loginData: LoginData) {
        this.loginData = loginData
        with(loginData) {
            val refresh_token = loginData.refresh_token
        }
    }
}