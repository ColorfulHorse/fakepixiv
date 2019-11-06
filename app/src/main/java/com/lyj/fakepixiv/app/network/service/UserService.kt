package com.lyj.fakepixiv.app.network.service

import com.lyj.fakepixiv.app.constant.Constant
import com.lyj.fakepixiv.app.constant.Restrict
import com.lyj.fakepixiv.app.data.model.response.LoginResp
import com.lyj.fakepixiv.app.data.model.response.UserInfo
import com.lyj.fakepixiv.app.data.model.response.UserPreviewListResp
import com.lyj.fakepixiv.app.network.retrofit.interceptors.SwitchBaseUrlInterceptor
import io.reactivex.Observable
import retrofit2.http.*

/**
 * @author green sun
 *
 * @date 2019/10/16
 *
 * @desc
 */
interface UserService {

    /**
     * 登录
     * header 用于切换baseUrl [SwitchBaseUrlInterceptor]
     * [grantType] 登录方式，账号密码或refreshToken登录二选一
     * @Field("client_id") clientId: String = "MOBrBDS8blbauoSck0ZfDbtuzpyT",
     * @Field("client_secret")clientSecret: String = "lsACyCD94FhDUtGTXi3QzcFE2uU1hqtDaKeqrdwj",
     */
    @Headers("SWITCH-HEADER:TAG_AUTH")
    @POST("/auth/token")
    @FormUrlEncoded
    fun login(@Field("client_id") clientId: String = "MOBrBDS8blbauoSck0ZfDbtuzpyT",
              @Field("client_secret")clientSecret: String = "lsACyCD94FhDUtGTXi3QzcFE2uU1hqtDaKeqrdwj",
              @Field("get_secure_url")get_secure_url: Boolean = true,
              @Field("include_policy")include_policy: Boolean = true,
              @Field("grant_type")grantType: String = Constant.Net.GRANT_TYPE_PWD,
              @Field("username")userName: String = "",
              @Field("password")password: String = "",
              @Field("device_token")deviceToken: String = "",
              @Field("refresh_token")refreshToken: String = ""): Observable<LoginResp>

    // https://accounts.pixiv.net/api/provisional-accounts/create
    // user_name=%E5%8C%97%E9%87%8E%E9%9D%92%E9%98%B3&ref=pixiv_android_app_provisional_account

    // https://accounts.pixiv.net/api/account/edit
    // new_mail_address=812194178%40qq.com&new_user_account=liaoyijian&current_password=3jeaTcVEUB&new_password=liaolove1314  new_user_account=beiyeqingyang&current_password=liaolove1314
    // https://app-api.pixiv.net/v1/user/me/state

    /**
     * 最新-推荐用户
     */
    @GET("/v1/user/recommended")
    suspend fun getUserRecommend(): UserPreviewListResp


    /**
     * 粉丝
     */
    @GET("/v1/user/follower")
    suspend fun getFollower(@Query("user_id") userId: String): UserPreviewListResp

    /**
     * 好P友
     */
    @GET("/v1/user/mypixiv")
    suspend fun getMypixiv(@Query("user_id") userId: String): UserPreviewListResp

    /**
     * 关注的人
     */
    @GET("/v1/user/following")
    suspend fun getFollowing(@Query("user_id") userId: String, @Query("restrict")restrict: String): UserPreviewListResp

    /**
     * 获取用户详情
     */
    @GET("/v1/user/detail")
    suspend fun getUserDetail(@Query("user_id")userId: String): UserInfo


    /**
     * 获取相关用户
     */
    @GET("/v1/user/related")
    fun getRelatedUsers(@Query("seed_user_id")userId: String): Observable<UserPreviewListResp>

    /**
     * 搜索用户
     */
    @GET("/v1/search/user")
    suspend fun searchUser(@Query("word")keyword: String): UserPreviewListResp

    /**
     * 关注
     */
    @POST("/v1/user/follow/add")
    @FormUrlEncoded
    fun followUser(@Field("user_id")userId: String,
                   @Restrict @Field("restrict") restrict: String = Restrict.PUBLIC): Observable<Any>

    @POST("/v1/user/follow/delete")
    @FormUrlEncoded
    fun unFollowUser(@Field("user_id")userId: String): Observable<Any>



    /**
     * 更多用户
     */
    @GET
    suspend fun getMoreUser(@Url nextUrl: String): UserPreviewListResp
}