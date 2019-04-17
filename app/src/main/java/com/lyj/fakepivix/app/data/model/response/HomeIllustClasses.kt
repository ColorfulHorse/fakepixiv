package com.lyj.fakepivix.app.data.model.response

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
//data class Illust(
//        val caption: String = "",
//        val create_date: String = "",
//        val height: Int = 0,
//        val id: Int = 0,
//        val image_urls: ImageUrls = ImageUrls(),
//        val is_bookmarked: Boolean = false,
//        val is_muted: Boolean = false,
//        val meta_pages: List<Any> = listOf(),
//        val meta_single_page: MetaSinglePage = MetaSinglePage(),
//        val page_count: Int = 0,
//        val restrict: Int = 0,
//        val sanity_level: Int = 0,
//        val series: Any = Any(),
//        val tags: List<TagX> = listOf(),
//        val title: String = "",
//        val tools: List<Any> = listOf(),
//        val total_bookmarks: Int = 0,
//        val total_view: Int = 0,
//        val type: String = "",
//        val user: User? = null,
//        val visible: Boolean = false,
//        val width: Int = 0,
//        val x_restrict: Int = 0
//)

data class TagX(
        val name: String = "",
        val translated_name: Any = Any()
)

/**
 * 主页人气直播response
 *
 * @property live_info
 * @property lives
 * @property next_url
 */
data class RankLiveResp(
    val live_info: Any?,
    val lives: List<Live> = listOf(),
    val next_url: String = ""
)

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
