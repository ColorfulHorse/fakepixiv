package com.lyj.fakepivix.app.data.model.response

import com.squareup.moshi.JsonClass

/**
 * @author greensun
 *
 * @date 2019/4/14
 *
 * @desc
 */

/**
 * 主页插画排行榜response
 */

@JsonClass(generateAdapter = true)
data class TagX(
        val name: String = "",
        val translated_name: Any = Any()
)

/**
 * 主页人气直播response
 *
 * @property live_info
 * @property lives
 * @property next_url  下一部分
 */

@JsonClass(generateAdapter = true)
data class RankLiveResp(
    val live_info: Any?,
    val lives: List<Live> = listOf(),
    val next_url: String = ""
)

@JsonClass(generateAdapter = true)
data class Live(
    val channel_id: String = "",
    val created_at: String = "",
    val id: String = "",
    val is_adult: Boolean = false,
    val is_closed: Boolean = false,
    val is_enabled_mic_input: Boolean = false,
    val is_muted: Boolean = false,
    val is_r15: Boolean = false,
    val is_r18: Boolean = false,
    val is_single: Boolean = false,
    val member_count: Int = 0,
    val mode: String = "",
    val name: String = "",
    val owner: Owner = Owner(),
    val performer_count: Int = 0,
    val performers: List<Any> = listOf(),
    val publicity: String = "",
    val server: String = "",
    val thumbnail_image_url: String = "",
    val total_audience_count: Int = 0
)

@JsonClass(generateAdapter = true)
data class Owner(
    val user: User = User()
)


/**
 *
 *主页pixivision response
 * @property next_url
 * @property spotlight_articles
 */
data class SpotLightResp(
    val next_url: String = "",
    val spotlight_articles: List<SpotlightArticle> = listOf()
)

data class SpotlightArticle(
    val article_url: String = "",
    val category: String = "",
    val id: Long = 0,
    val publish_date: String = "",
    val pure_title: String = "",
    val subcategory_label: String = "",
    val thumbnail: String = "",
    val title: String = ""
)
