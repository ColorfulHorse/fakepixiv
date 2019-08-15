package com.lyj.fakepivix.app.data.source.remote

import android.databinding.ObservableField
import android.util.ArrayMap
import android.util.SparseArray
import com.lyj.fakepivix.app.constant.Constant
import com.lyj.fakepivix.app.constant.Restrict
import com.lyj.fakepivix.app.data.model.response.*
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.app.network.retrofit.RetrofitManager
import com.lyj.fakepivix.app.reactivex.schedulerTransform
import com.lyj.fakepivix.app.utils.SPUtil
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy

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

    val users = ArrayMap<String, User>()

    operator fun get(key: String): User? {
        return users[key]
    }

    operator fun set(key: String, value: User) {
        users[key] = value
    }

    operator fun minus(key: String) {
        users.remove(key)
    }

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

    suspend fun getRecommendUsers(): UserPreviewListResp {
        return RetrofitManager.instance
                .apiService
                .getUserRecommend()
    }

    /**
     * 搜索用户
     */
    suspend fun searchUser(keyword: String): UserPreviewListResp = RetrofitManager.instance
            .apiService
            .searchUser(keyword)


    /**
     * 用户详情
     */
    suspend fun getUserInfo(userId: String): UserInfo = RetrofitManager.instance
            .apiService
            .getUserDetail(userId)


    /**
     * 获取相关用户
     */
    fun getRelatedUsers(userId: String): Observable<UserPreviewListResp> {
        return RetrofitManager.instance
                .apiService
                .getRelatedUsers(userId)
                .schedulerTransform()
    }

    /**
     *  关注用户
     */
    fun follow(userId: String, follow: Boolean, @Restrict restrict: String = Restrict.PUBLIC): Observable<Any> {
        return if (follow) RetrofitManager.instance
                .apiService
                .followUser(userId, restrict)
                .schedulerTransform()
        else RetrofitManager.instance
                .apiService
                .unFollowUser(userId)
                .schedulerTransform()
    }

    /**
     *  关注用户
     */
    fun follow(user: User, loadState: ObservableField<LoadState>, @Restrict restrict: String = Restrict.PUBLIC): Disposable? {

        if (loadState.get() !is LoadState.Loading) {
            val followed = user.is_followed
            return  instance
                    .follow(user.id, !followed, restrict)
                    .doOnSubscribe { loadState.set(LoadState.Loading) }
                    .subscribeBy(onNext = {
                        user.is_followed = !followed
                        loadState.set(LoadState.Succeed)
                    }, onError = {
                        loadState.set(LoadState.Failed(it))
                    })
        }
        return null
    }

    /**
     * 获取更多用户
     */
    suspend fun loadMore(url: String): UserPreviewListResp = RetrofitManager.instance
            .apiService
            .getMoreUser(url)

}