package com.lyj.fakepixiv.app.data.source.remote

import android.databinding.ObservableField
import android.util.ArrayMap
import com.lyj.fakepixiv.app.constant.Constant
import com.lyj.fakepixiv.app.constant.Restrict
import com.lyj.fakepixiv.app.data.model.response.LoginData
import com.lyj.fakepixiv.app.data.model.response.User
import com.lyj.fakepixiv.app.data.model.response.UserInfo
import com.lyj.fakepixiv.app.data.model.response.UserPreviewListResp
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.app.network.retrofit.RetrofitManager
import com.lyj.fakepixiv.app.network.service.UserService
import com.lyj.fakepixiv.app.reactivex.schedulerTransform
import com.lyj.fakepixiv.app.utils.SPUtil
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.*

/**
 * @author greensun
 *
 * @date 2019/4/10
 *
 * @desc
 */
class UserRepository private constructor() {

    private val service: UserService by lazy { RetrofitManager.instance.userService }

    companion object {
        @JvmStatic
        val instance: UserRepository by lazy { UserRepository() }
    }

    var loginData: LoginData? = null

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

//    fun login(userName: String, password: String, deviceToken: String = ""): Observable<LoginData> {
//        return service
//                //.login(refreshToken = "q3GIjHG94qZamlmGpjoJmUW7mA9ziFN8k9GYmYA44fY", grantType = Constant.Net.GRANT_TYPE_TOKEN)
//                .login(userName = userName, password = password, deviceToken = deviceToken)
//                .map { it.response }
//                .doOnNext {
//                    accessToken = "Bearer ${it.access_token}"
//                    loginData = it
//                    SPUtil.saveLoginData(it)
//                }
//                .schedulerTransform()
//
//    }

    /**
     * 登录
     * [provisional]是否临时用户
     */
    suspend fun login(userName: String, password: String, deviceToken: String = "", provisional: Boolean = false): LoginData {
        return withContext(Dispatchers.IO) {
            val resp = service.login(userName = userName, password = password, deviceToken = deviceToken).response
            resp.provisional = provisional
            loginData = resp
            SPUtil.saveLoginData(resp)
            resp
        }
    }

    /**
     * 用refreshToken登陆
     */
    suspend fun reLogin(cache: LoginData): LoginData {
        //this.loginData = cache
        with(cache) {
            return withContext(Dispatchers.IO) {
                val resp = service
                        .login(grantType = Constant.Net.GRANT_TYPE_TOKEN, refreshToken = refresh_token, deviceToken = device_token)
                        .response
                resp.provisional = cache.provisional
                loginData = resp
                SPUtil.saveLoginData(resp)
                resp
            }
        }
    }

    suspend fun register(userName: String): ProvisionAccountResp {
        return service.register(userName)
    }

    /**
     * 同步刷新token
     */
    fun refreshToken(cache: LoginData): LoginData? {
        with(cache) {
            val call = service.refreshToken(grantType = Constant.Net.GRANT_TYPE_TOKEN,
                    refreshToken = refresh_token, deviceToken = device_token)
            call.execute().body()?.let {
                it.response.provisional = cache.provisional
                it.response.lastRefreshTime = cache.lastRefreshTime
                loginData = it.response
                SPUtil.saveLoginData(it.response)
                return it.response
            }
            return null
        }
    }

    suspend fun getRecommendUsers(): UserPreviewListResp {
        return service
                .getUserRecommend()
    }

    suspend fun getFollowing(userId: String, @Restrict restrict: String): UserPreviewListResp {
        return service.getFollowing(userId, restrict)
    }

    /**
     * 搜索用户
     */
    suspend fun searchUser(keyword: String): UserPreviewListResp = service.searchUser(keyword)


    /**
     * 用户详情
     */
    suspend fun getUserInfo(userId: String): UserInfo = service.getUserDetail(userId)


    /**
     * 获取相关用户
     */
    fun getRelatedUsers(userId: String): Observable<UserPreviewListResp> {
        return service
                .getRelatedUsers(userId)
                .schedulerTransform()
    }

    /**
     *  关注用户
     */
    fun follow(userId: String, follow: Boolean, @Restrict restrict: String = Restrict.PUBLIC): Observable<Any> {
        return if (follow) service
                .followUser(userId, restrict)
                .schedulerTransform()
        else service
                .unFollowUser(userId)
                .schedulerTransform()
    }

    /**
     *  关注用户
     */
    fun follow(user: User, loadState: ObservableField<LoadState>, @Restrict restrict: String = Restrict.PUBLIC): Disposable? {

        if (loadState.get() !is LoadState.Loading) {
            val followed = user.is_followed
            return instance
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
    suspend fun loadMore(url: String): UserPreviewListResp = service.getMoreUser(url)

}