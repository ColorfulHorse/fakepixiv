package com.lyj.fakepivix.app.data.model.response

import com.squareup.moshi.JsonClass

/**
 * @author greensun
 *
 * @date 2019/4/14
 *
 * @desc 小说
 */


@JsonClass(generateAdapter = true)
data class NovelListResp(
    val contest_exists: Boolean = false,
    val novels: List<Illust> = listOf(),
    val next_url: String = "",
    val privacy_policy: PrivacyPolicy = PrivacyPolicy(),
    val ranking_novels: List<Illust> = listOf()
)

/**
 * 小说内容 [newpage] 分页
 */
@JsonClass(generateAdapter = true)
data class NovelText(
        val novel_marker: NovelMarker? = null,
        var novel_text: String = "",
        val series_next: NovelChapter? = null,
        val series_prev: NovelChapter? = null
) {

    companion object {
        const val SEPARATOR = "[newpage]"
    }

    fun getNovel(): List<String> {
        val res = novel_text.split(SEPARATOR)
        novel_text = ""
        return res
    }
}

@JsonClass(generateAdapter = true)
data class NovelChapter(
    val caption: String = "",
    val create_date: String = "",
    val id: Long = 0,
    val image_urls: ImageUrls = ImageUrls(),
    val is_bookmarked: Boolean = false,
    val is_muted: Boolean = false,
    val is_mypixiv_only: Boolean = false,
    val is_x_restricted: Boolean = false,
    val page_count: Int = 0,
    val restrict: Int = 0,
    val series: Series = Series(),
    val tags: List<Tag> = listOf(),
    val text_length: Int = 0,
    val title: String = "",
    val total_bookmarks: Int = 0,
    val total_comments: Int = 0,
    val total_view: Int = 0,
    val user: User = User(),
    val visible: Boolean = false,
    val x_restrict: Int = 0
)

data class NovelMarker(
    val page: Int = 0
)

