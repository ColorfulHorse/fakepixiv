package com.lyj.fakepivix.app.data.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author greensun
 *
 * @date 2019/9/28
 *
 * @desc
 */
@JsonClass(generateAdapter = true)
data class NovelSeries(
    val novel_series_detail: NovelSeriesDetail = NovelSeriesDetail(),
    @Json(name = "novel_series_first_novel")
    val first: Illust = Illust(),
    @Json(name = "novel_series_latest_novel")
    val last: Illust = Illust(),
    val novels: List<Illust> = listOf(),
    val next_url: String = ""
)

@JsonClass(generateAdapter = true)
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
