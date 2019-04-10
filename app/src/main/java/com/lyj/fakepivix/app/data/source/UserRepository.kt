package com.lyj.fakepivix.app.data.source

import com.lyj.fakepivix.app.data.model.response.LoginData
import com.lyj.fakepivix.app.data.model.response.User
import com.lyj.fakepivix.app.network.ApiException
import com.lyj.fakepivix.app.network.ApiService
import com.lyj.fakepivix.app.network.retrofit.RetrofitManager
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

    var user: User? = null
    var accessToken = ""

    fun getUserInfo(userName: String, password: String): Observable<LoginData> {
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
                    accessToken = it.access_token
                    user = it.user
                }

    }
}