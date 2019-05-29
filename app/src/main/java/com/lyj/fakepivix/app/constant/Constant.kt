package com.lyj.fakepivix.app.constant

/**
 * @author 19930
 *
 * @date 2018/12/21
 *
 * @desc
 */
interface Constant {
    object Net {

        const val BASE_URL = "https://app-api.pixiv.net"
        const val AUTH_URL = "https://oauth.secure.pixiv.net"

        const val SWITCH_HEADER = "SWITCH-HEADER" // 用于分辨是否需要切换baseUrl
        const val TAG_BASE = "BASE_URL"
        const val TAG_AUTH = "TAG_AUTH"

        const val GRANT_TYPE_PWD = "password"
        const val GRANT_TYPE_TOKEN = "refresh_token"
        const val HEADER_TOKEN = "Authorization"
    }

    object SP {
        const val KEY_LOGIN_CACHE = "KEY_LOGIN_CACHE"

        const val KEY_RESTRICT_ILLUST = "KEY_RESTRICT_ILLUST"

        const val KEY_RESTRICT_NOVEL = "KEY_RESTRICT_NOVEL"
    }
}