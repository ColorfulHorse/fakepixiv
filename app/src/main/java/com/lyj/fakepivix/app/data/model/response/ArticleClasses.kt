package com.lyj.fakepivix.app.data.model.response

import com.squareup.moshi.JsonClass

/**
 * @author greensun
 *
 * @date 2019/3/20
 *
 * @desc
 */

/**
 *
 * 特辑 response
 * @property next_url
 * @property spotlight_articles
 */
@JsonClass(generateAdapter = true)
data class SpotLightResp(
        val next_url: String = "",
        val spotlight_articles: List<SpotlightArticle> = listOf()
)

// https://www.pixivision.net/zh/a/5073 HTTP/1.1
@JsonClass(generateAdapter = true)
data class SpotlightArticle(
        val article_url: String = "",
        val category: String = "",
        val id: Long = 0,
        val publish_date: String = "",
        val pure_title: String = "",
        // 采访（橙色） 插画
        val subcategory_label: String = "",
        val thumbnail: String = "",
        val title: String = ""
)

