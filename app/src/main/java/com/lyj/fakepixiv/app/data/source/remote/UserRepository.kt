package com.lyj.fakepixiv.app.data.source.remote

import android.util.ArrayMap
import androidx.databinding.ObservableField
import com.lyj.fakepixiv.app.constant.Constant
import com.lyj.fakepixiv.app.constant.Restrict
import com.lyj.fakepixiv.app.data.model.response.*
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.app.network.retrofit.RetrofitManager
import com.lyj.fakepixiv.app.network.service.UserService
import com.lyj.fakepixiv.app.reactivex.io
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

    val service: UserService by lazy { RetrofitManager.instance.userService }

    companion object {
        @JvmStatic
        val instance: UserRepository by lazy { UserRepository() }
    }

    var loginData: LoginData? = null

    val users = ArrayMap<Long, User>()

    operator fun get(key: Long): User? {
        return users[key]
    }

    operator fun set(key: Long, value: User) {
        users[key] = value
    }

    operator fun minus(key: Long) {
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
//                .io()
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
            if (provisional) {
                // 临时用户存密码
                resp.user.password = password
            }
            loginData = resp
            SPUtil.saveLoginData(resp)
            val emoji = CommonRepository.instance
                    .service
                    .getEmoji()
            SPUtil.save(Constant.SP.KEY_EMOJI, emoji)
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
                var resp: LoginData
                val password = cache.user.password
                resp = if (password.isNotBlank() && !cache.provisional) {
                    // 临时账号更改了信息以后需要重新用账号密码登录，并清除掉密码
                    service.login(userName = cache.user.account, password = password, deviceToken = device_token).response.apply {
                        cache.user.password = ""
                    }
                }else {
                    service
                            .login(grantType = Constant.Net.GRANT_TYPE_TOKEN, refreshToken = refresh_token, deviceToken = device_token)
                            .response
                }
                resp = resp.copy(provisional = cache.provisional, user = user.copy(password = cache.user.password))
                loginData = resp
                SPUtil.saveLoginData(resp)
                val emoji = CommonRepository.instance
                        .service
                        .getEmoji()
                SPUtil.save(Constant.SP.KEY_EMOJI, emoji)
                resp
            }
        }
    }

    /**
     * 创建临时账户
     */
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
                val resp = it.response.copy(provisional = cache.provisional,
                        user = user.copy(password = cache.user.password),
                        lastRefreshTime = cache.lastRefreshTime)
                loginData = resp
                SPUtil.saveLoginData(resp)
                return resp
            }
            return null
        }
    }

    suspend fun getRecommendUsers(): UserPreviewListResp {
        return service
                .getUserRecommend()
    }

    /**
     * 关注的人
     */
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
    suspend fun getUserInfo(userId: Long): UserInfo = service.getUserDetail(userId)


    /**
     * 获取相关用户
     */
    fun getRelatedUsers(userId: Long): Observable<UserPreviewListResp> {
        return service
                .getRelatedUsers(userId)
                .io()
    }

    /**
     *  关注用户
     */
    fun follow(userId: Long, follow: Boolean, @Restrict restrict: String = Restrict.PUBLIC): Observable<Any> {
        return if (follow) service
                .followUser(userId, restrict)
                .io()
        else service
                .unFollowUser(userId)
                .io()
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