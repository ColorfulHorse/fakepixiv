package com.lyj.fakepixiv.app.network.service

import com.lyj.fakepixiv.app.constant.Constant
import com.lyj.fakepixiv.app.constant.Restrict
import com.lyj.fakepixiv.app.data.model.response.*
import com.lyj.fakepixiv.app.network.retrofit.interceptors.SwitchBaseUrlInterceptor
import io.reactivex.Observable
import retrofit2.Call
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
    suspend fun login(@Field("client_id") clientId: String = "MOBrBDS8blbauoSck0ZfDbtuzpyT",
                      @Field("client_secret")clientSecret: String = "lsACyCD94FhDUtGTXi3QzcFE2uU1hqtDaKeqrdwj",
                      @Field("get_secure_url")get_secure_url: Boolean = true,
                      @Field("include_policy")include_policy: Boolean = true,
                      @Field("grant_type")grantType: String = Constant.Net.GRANT_TYPE_PWD,
                      @Field("username")userName: String = "",
                      @Field("password")password: String = "",
                      @Field("device_token")deviceToken: String = "",
                      @Field("refresh_token")refreshToken: String = ""): LoginResp

    @Headers("SWITCH-HEADER:TAG_AUTH")
    @POST("/auth/token")
    @FormUrlEncoded
    suspend fun loginV2(@Field("code") code: String = "",
                        @Field("code_verifier") code_verifier: String = "",
                        @Field("get_secure_url") get_secure_url: Boolean = true,
                        @Field("include_policy") include_policy: Boolean = true,
                        @Field("grant_type") grantType: String = Constant.Net.GRANT_TYPE_CODE,
                        @Field("client_id") clientId: String = "MOBrBDS8blbauoSck0ZfDbtuzpyT",
                        @Field("redirect_uri") redirect_uri: String = "https://app-api.pixiv.net/web/v1/users/auth/pixiv/callback",
                        @Field("client_secret") clientSecret: String = "lsACyCD94FhDUtGTXi3QzcFE2uU1hqtDaKeqrdwj",
                        @Field("device_token") deviceToken: String = "",
                        @Field("refresh_token") refreshToken: String = ""): LoginResp

    @Headers("SWITCH-HEADER:TAG_AUTH")
    @POST("/auth/token")
    @FormUrlEncoded
    fun refreshToken(@Field("client_id") clientId: String = "MOBrBDS8blbauoSck0ZfDbtuzpyT",
                      @Field("client_secret")clientSecret: String = "lsACyCD94FhDUtGTXi3QzcFE2uU1hqtDaKeqrdwj",
                      @Field("get_secure_url")get_secure_url: Boolean = true,
                      @Field("include_policy")include_policy: Boolean = true,
                      @Field("grant_type")grantType: String = Constant.Net.GRANT_TYPE_PWD,
                      @Field("username")userName: String = "",
                      @Field("password")password: String = "",
                      @Field("device_token")deviceToken: String = "",
                      @Field("refresh_token")refreshToken: String = ""): Call<LoginResp>


    /**
     * 注册，创建临时账户
     */
    @Headers("SWITCH-HEADER:TAG_ACCOUNT", "Authorization:Bearer l-f9qZ0ZyqSwRyZs8-MymbtWBbSxmCu1pmbOlyisou8")
    @POST("/api/provisional-accounts/create")
    @FormUrlEncoded
    suspend fun register(@Field("user_name")userName: String,
                         @Field("ref")ref: String = "pixiv_android_app_provisional_account"): ProvisionAccountResp


    // https://accounts.pixiv.net/api/account/edit
    // new_mail_address=812194178%40qq.com&
    // new_user_account=liaoyijian&current_password=3jeaTcVEUB&new_password=liaolove1314
    // new_user_account=beiyeqingyang&current_password=liaolove1314
    // https://app-api.pixiv.net/v1/user/me/state

    /**
     * 获取账户信息
     */
    @GET("/v1/user/me/state")
    suspend fun getAccountState(): AccountStateResp


    /**
     * 修改账户信息
     */
    @POST("/api/v2/account/edit")
    @Headers("SWITCH-HEADER:TAG_ACCOUNT")
    @FormUrlEncoded
    suspend fun editAccount(@Field("new_mail_address")mail: String = "",
                            @Field("new_user_account")account: String = "",
                            @Field("current_password")current_password: String = "",
                            @Field("new_password")new_password: String = ""): EditAccountResp

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
    suspend fun getUserDetail(@Query("user_id")userId: Long): UserInfo


    /**
     * 获取相关用户
     */
    @GET("/v1/user/related")
    fun getRelatedUsers(@Query("seed_user_id")userId: Long): Observable<UserPreviewListResp>

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
    fun followUser(@Field("user_id")userId: Long,
                   @Restrict @Field("restrict") restrict: String = Restrict.PUBLIC): Observable<Any>

    @POST("/v1/user/follow/delete")
    @FormUrlEncoded
    fun unFollowUser(@Field("user_id")userId: Long): Observable<Any>



    /**
     * 更多用户
     */
    @GET
    suspend fun getMoreUser(@Url nextUrl: String): UserPreviewListResp
}