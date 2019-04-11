package com.lyj.fakepivix.app.network

import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.App

/**
 * @author greensun
 *
 * @date 2019/3/21
 *
 * @desc
 */
class ApiException constructor(var code: Int) : Exception() {

    constructor() : this(CODE_UNKNOWN)

    override val message: String?
        get() = getMessage(code)

    companion object {
        const val CODE_SUCCESS = 100
        const val CODE_EMPTY_DATA = 400
        const val CODE_UNKNOWN = 9999

        const val CODE_ACCOUNT_EERROR = 1508

        const val MESSAGE_SUCCESS = "成功"
        const val MESSAGE_EMPTY_DATA = "暂时没有数据"
        const val MESSAGE_UNKNOWN = "未知错误"
        const val MESSAGE_ACCOUNT_EERROR = "账号或密码错误"
    }

    private fun getMessage(code: Int): String =
        when(code) {
            CODE_EMPTY_DATA ->  MESSAGE_EMPTY_DATA
            else -> MESSAGE_SUCCESS
        }

}