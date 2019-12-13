package com.lyj.fakepixiv.app.data.model.response

import com.squareup.moshi.JsonClass

/**
 * @author greensun
 *
 * @date 2019/11/5
 *
 * @desc
 */
data class EmojiResp(
    val emoji_definitions: List<Emoji> = listOf()
)

data class Emoji(
    val id: Int = 0,
    val slug: String = "",
    val image_url_medium: String = ""
)

/**
 * 历史记录
 */
@JsonClass(generateAdapter = true)
data class History(val view_time: Long, val illust: Illust)

@JsonClass(generateAdapter = true)
data class HistoryListResp(
        val code: Int = 200,
        val data: List<History> = listOf(),
        val pageNo: Int = 1,
        val pageSize: Int = 20, val total: Long = 0)

@JsonClass(generateAdapter = true)
data class PatchResp(
        val url: String = "",
        var hasPatch: Boolean)