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
        // 排行榜类型
        val ILLUST_RANK_MODES = listOf(
                "day",
                "day_male",
                "day_female",
                "week_original",
                "week_rookie",
                "week",
                "month",
                "day")
        val COMIC_RANK_MODES = listOf(
                "day_manga",
                "week_rookie_manga",
                "week_manga",
                "month_manga",
                "day_manga")

        val NOVEL_RANK_MODES = listOf(
                "day",
                "day_male",
                "day_female",
                "week_rookie",
                "week",
                "day")

        const val BASE_URL = "https://app-api.pixiv.net"
        const val AUTH_URL = "https://oauth.secure.pixiv.net"

        const val SWITCH_HEADER = "SWITCH-HEADER" // 用于分辨是否需要切换baseUrl
        const val TAG_BASE = "BASE_URL"
        const val TAG_AUTH = "TAG_AUTH"

        const val GRANT_TYPE_PWD = "password"
        const val GRANT_TYPE_TOKEN = "refresh_token"
        const val HEADER_TOKEN = "Authorization"
    }

    object Request {
        const val KEY_DATE_DESC = "date_desc"
        const val KEY_DATE_ASC = "date_asc"

        // 模糊搜索
        const val KEY_SEARCH_PARTIAL = "partial_match_for_tags"
        // 精确搜索
        const val KEY_SEARCH_EXCAT = "exact_match_for_tags"
        // 按标题搜索
        const val KEY_SEARCH_TITLE_CAPTION = "title_and_caption"
    }

    object SP {
        const val KEY_LOGIN_CACHE = "KEY_LOGIN_CACHE"

        const val KEY_RESTRICT_ILLUST = "KEY_RESTRICT_ILLUST"

        const val KEY_RESTRICT_NOVEL = "KEY_RESTRICT_NOVEL"
    }
}