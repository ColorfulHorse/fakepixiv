package com.lyj.fakepivix.app.data.model.response

/**
 * @author greensun
 *
 * @date 2019/4/9
 *
 * @desc
 */


data class LoginResp(
        val response: Response = Response()
)

data class Response(
        // 需要放在头部的tokenBearer JbMfFwZxLlC7dLz15qVHCg50yni0wySy-wxdKyZHCYM
        val access_token: String = "",
        val device_token: String = "",
        val expires_in: Int = 0,
        val refresh_token: String = "",
        val scope: String = "",
        val token_type: String = "",
        val user: User = User()
)
