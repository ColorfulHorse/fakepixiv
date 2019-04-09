package com.lyj.fakepivix.app.data.model.response

/**
 * @author greensun
 *
 * @date 2019/3/20
 *
 * @desc
 */

data class IllustListResp(
    val contest_exists: Boolean = false,
    val illusts: List<Illust> = listOf(),
    val next_url: String = "",
    val privacy_policy: PrivacyPolicy = PrivacyPolicy(),
    val ranking_illusts: List<RankingIllust> = listOf()
)

data class Illust(
    val caption: String = "",
    val create_date: String = "",
    val height: Int = 0,
    val id: Int = 0,
    val image_urls: ImageUrls = ImageUrls(),
    val is_bookmarked: Boolean = false,
    val is_muted: Boolean = false,
    val meta_pages: List<Any> = listOf(),
    val meta_single_page: MetaSinglePage = MetaSinglePage(),
    val page_count: Int = 0,
    val restrict: Int = 0,
    val sanity_level: Int = 0,
    val series: Any = Any(),
    val tags: List<Tag> = listOf(),
    val title: String = "",
    val tools: List<Any> = listOf(),
    val total_bookmarks: Int = 0,
    val total_view: Int = 0,
    val type: String = "",
    val user: User = User(),
    val visible: Boolean = false,
    val width: Int = 0,
    val x_restrict: Int = 0
)

data class User(
    val account: String = "",
    val id: Int = 0,
    val is_followed: Boolean = false,
    val name: String = "",
    val profile_image_urls: ProfileImageUrls = ProfileImageUrls()
)

data class ProfileImageUrls(
    val medium: String = ""
)

data class MetaSinglePage(
    val original_image_url: String = ""
)

data class Tag(
    val name: String = "",
    val translated_name: Any = Any()
)

data class ImageUrls(
    val large: String = "",
    val medium: String = "",
    val square_medium: String = ""
)

/**
 * 插画排行榜
 */
data class RankingIllust(
    val caption: String = "",
    val create_date: String = "",
    val height: Int = 0,
    val id: Int = 0,
    val image_urls: ImageUrls = ImageUrls(),
    val is_bookmarked: Boolean = false,
    val is_muted: Boolean = false,
    val meta_pages: List<Any> = listOf(),
    val meta_single_page: MetaSinglePage = MetaSinglePage(),
    val page_count: Int = 0,
    val restrict: Int = 0,
    val sanity_level: Int = 0,
    val series: Any = Any(),
    val tags: List<TagX> = listOf(),
    val title: String = "",
    val tools: List<Any> = listOf(),
    val total_bookmarks: Int = 0,
    val total_view: Int = 0,
    val type: String = "",
    val user: User = User(),
    val visible: Boolean = false,
    val width: Int = 0,
    val x_restrict: Int = 0
)

data class TagX(
    val name: String = "",
    val translated_name: Any = Any()
)

data class PrivacyPolicy(
    val message: String = "",
    val url: String = "",
    val version: String = ""
)
