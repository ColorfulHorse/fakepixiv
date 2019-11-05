package com.lyj.fakepixiv.app.data.model.response

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