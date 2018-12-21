package com.lyj.fakepivix.model.response

/**
 * @author 19930
 *
 * @date 2018/12/21
 *
 * @desc
 */
data class IllustListResp(
    val illusts: List<Illust>,
    val next_url: String
)

data class Illust(
    val caption: String,
    val create_date: String,
    val height: Int,
    val id: Int,
    val image_urls: ImageUrls,
    val is_bookmarked: Boolean,
    val is_muted: Boolean,
    val meta_pages: List<Any>,
    val meta_single_page: MetaSinglePage,
    val page_count: Int,
    val restrict: Int,
    val sanity_level: Int,
    val series: Any?,
    val tags: List<Tag>,
    val title: String,
    val tools: List<String>,
    val total_bookmarks: Int,
    val total_view: Int,
    val type: String,
    val user: User,
    val visible: Boolean,
    val width: Int,
    val x_restrict: Int
)


data class MetaSinglePage(
    val original_image_url: String
)

data class Tag(
    val name: String
)


// 作品详情
data class IllustResp(val illust: Illust)

