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
