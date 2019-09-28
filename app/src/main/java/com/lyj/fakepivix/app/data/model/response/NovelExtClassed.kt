package com.lyj.fakepivix.app.data.model.response

/**
 * @author greensun
 *
 * @date 2019/9/28
 *
 * @desc
 */
data class NovelSeries(
    val novel_series_detail: NovelSeriesDetail = NovelSeriesDetail(),
    val novel_series_first_novel: NovelSeriesFirstNovel = NovelSeriesFirstNovel(),
    val novel_series_latest_novel: NovelSeriesLatestNovel = NovelSeriesLatestNovel(),
    val novels: List<Novel> = listOf(),
    val next_url: String = ""
)

data class NovelSeriesFirstNovel(
    val id: Int = 0,
    val title: String = "",
    val caption: String = "",
    val restrict: Int = 0,
    val x_restrict: Int = 0,
    val image_urls: ImageUrls = ImageUrls(),
    val create_date: String = "",
    val tags: List<Tag> = listOf(),
    val page_count: Int = 0,
    val text_length: Int = 0,
    val user: User = User(),
    val series: Series = Series(),
    val is_bookmarked: Boolean = false,
    val total_bookmarks: Int = 0,
    val total_view: Int = 0,
    val visible: Boolean = false,
    val total_comments: Int = 0,
    val is_muted: Boolean = false,
    val is_mypixiv_only: Boolean = false,
    val is_x_restricted: Boolean = false
)

data class NovelSeriesDetail(
    val id: Int = 0,
    val title: String = "",
    val caption: String = "",
    val is_original: Boolean = false,
    val is_concluded: Boolean = false,
    val content_count: Int = 0,
    val total_character_count: Int = 0,
    val user: User = User(),
    val display_text: String = ""
)

data class Novel(
    val id: Int = 0,
    val title: String = "",
    val caption: String = "",
    val restrict: Int = 0,
    val x_restrict: Int = 0,
    val image_urls: ImageUrls = ImageUrls(),
    val create_date: String = "",
    val tags: List<Tag> = listOf(),
    val page_count: Int = 0,
    val text_length: Int = 0,
    val user: User = User(),
    val series: Series = Series(),
    val is_bookmarked: Boolean = false,
    val total_bookmarks: Int = 0,
    val total_view: Int = 0,
    val visible: Boolean = false,
    val total_comments: Int = 0,
    val is_muted: Boolean = false,
    val is_mypixiv_only: Boolean = false,
    val is_x_restricted: Boolean = false
)

data class NovelSeriesLatestNovel(
    val id: Int = 0,
    val title: String = "",
    val caption: String = "",
    val restrict: Int = 0,
    val x_restrict: Int = 0,
    val image_urls: ImageUrls = ImageUrls(),
    val create_date: String = "",
    val tags: List<Tag> = listOf(),
    val page_count: Int = 0,
    val text_length: Int = 0,
    val user: User = User(),
    val series: Series = Series(),
    val is_bookmarked: Boolean = false,
    val total_bookmarks: Int = 0,
    val total_view: Int = 0,
    val visible: Boolean = false,
    val total_comments: Int = 0,
    val is_muted: Boolean = false,
    val is_mypixiv_only: Boolean = false,
    val is_x_restricted: Boolean = false
)