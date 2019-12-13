package com.lyj.fakepixiv.app.network

import java.io.IOException
import kotlin.Exception


/**
 * @author greensun
 *
 * @date 2019/3/21
 *
 * @desc
 */
class ApiException(val code: Int = CODE_UNKNOWN, override val message: String = map.getOrElse(code) { MESSAGE_UNKNOWN }) : IOException() {
    //    {
//        "has_error": true,
//        "errors": {
//        "system": {
//        "message": "103:pixiv ID、またはメールアドレス、パスワードが正しいかチェックしてください。",
//        "code": 1508
//    }
//    }
//    }
    companion object {
        const val CODE_SUCCESS = 100
        const val CODE_EMPTY_DATA = 400
        const val CODE_UNKNOWN = 9999
        const val CODE_TIMEOUT = 601
        const val CODE_CONNECT = 602
        const val CODE_SERVER_ERR = 501
        const val CODE_CLIENT_ERR = 502

        const val CODE_TOKEN_INVALID = 603

        const val CODE_ACCOUNT_EERROR = 1508

        const val CODE_NOT_VISIBLE = 666

        const val MESSAGE_SUCCESS = "成功"
        const val MESSAGE_EMPTY_DATA = "暂时没有数据"
        const val MESSAGE_UNKNOWN = "未知错误"
        const val MESSAGE_ACCOUNT_EERROR = "账号或密码错误"
        const val MESSAGE_TIMEOUT = "连接服务器超时"
        const val MESSAGE_CONNECT = "无法连接到服务器"
        const val MESSAGE_TOKEN_INVALID = "token无效"
        const val MESSAGE_NOT_VISIBLE = "插画不可见"
        const val MESSAGE_SERVER_ERR = "服务器异常"
        const val MESSAGE_CLIENT_ERR = "参数异常"

        val map = mapOf(
                CODE_SUCCESS to MESSAGE_SUCCESS,
                CODE_EMPTY_DATA to MESSAGE_EMPTY_DATA,
                CODE_ACCOUNT_EERROR to MESSAGE_ACCOUNT_EERROR,
                CODE_TIMEOUT to MESSAGE_TIMEOUT,
                CODE_TOKEN_INVALID to MESSAGE_TOKEN_INVALID,
                CODE_CONNECT to MESSAGE_CONNECT,
                CODE_NOT_VISIBLE to MESSAGE_NOT_VISIBLE,
                CODE_SERVER_ERR to MESSAGE_SERVER_ERR,
                CODE_CLIENT_ERR to MESSAGE_CLIENT_ERR
        )
    }

}