package com.lyj.fakepixiv.app.data.model.response

import com.squareup.moshi.JsonClass

/**
 * @author greensun
 *
 * @date 2019/4/9
 *
 * @desc
 */

@JsonClass(generateAdapter = true)
data class LoginResp(
        val response: LoginData = LoginData()
)

@JsonClass(generateAdapter = true)
data class LoginError(
        val has_error: Boolean = true,
        val errors: Errors = Errors()
)

@JsonClass(generateAdapter = true)
data class Errors(
        val system: SystemError = SystemError()
)

@JsonClass(generateAdapter = true)
data class SystemError(
        val code: Int = -1,
        val message: String = ""
)

@JsonClass(generateAdapter = true)
data class LoginData(
        // 需要放在头部的tokenBearer JbMfFwZxLlC7dLz15qVHCg50yni0wySy-wxdKyZHCYM
        val access_token: String = "",
        val device_token: String = "",
        val expires_in: Int = 0,
        val refresh_token: String = "",
        val scope: String = "",
        val token_type: String = "",
        val user: User = User(),
        // 避免并发请求重复刷新token
        var lastRefreshTime: Long = -1,
        // 是否临时账户
        var provisional: Boolean = false
) {
    fun needRefresh(): Boolean {
        val current = System.currentTimeMillis()
        if (current - lastRefreshTime > 5*60*1000 || lastRefreshTime == -1L) {
            lastRefreshTime = current
            return true
        }
        return false
    }
}

/**
 * 账户信息
 */
data class AccountStateResp(
        val user_state: AccountState = AccountState()
)

data class AccountState(
        val is_mail_authorized: Boolean = false,
        val has_changed_pixiv_id: Boolean = false,
        val can_change_pixiv_id: Boolean = false
)

/**
 * 创建的临时用户
 */
data class ProvisionAccountResp(
        val body: Account,
        val error: Boolean,
        val message: String
)

data class Account(
        val device_token: String,
        val password: String,
        val user_account: String
)


