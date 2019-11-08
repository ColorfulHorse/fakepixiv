package com.lyj.fakepixiv.app.data.model.response

import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.adapter.PreloadModel
import com.lyj.fakepixiv.app.application.ApplicationLike
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
): PreloadModel {
    override fun getPreloadUrls(): List<String> = listOf(thumbnail)

    companion object {
        const val SPOTLIGHT = "spotlight"
        const val INSPIRATION = "inspiration"
        const val TUTORIAL = "tutorial"
    }

    @ColorInt
    fun color(): Int {
        return when(category) {
            SPOTLIGHT -> ContextCompat.getColor(ApplicationLike.context, R.color.pixivision_category_spotlight)
            INSPIRATION -> ContextCompat.getColor(ApplicationLike.context, R.color.pixivision_category_inspiration)
            TUTORIAL -> ContextCompat.getColor(ApplicationLike.context, R.color.pixivision_category_tutorial)
            else -> ContextCompat.getColor(ApplicationLike.context, R.color.pixivision_category_spotlight)
        }
    }
}

