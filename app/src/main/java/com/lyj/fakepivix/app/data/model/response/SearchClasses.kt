package com.lyj.fakepivix.app.data.model.response

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.squareup.moshi.JsonClass

/**
 * @author greensun
 *
 * @date 2019/5/28
 *
 * @desc 搜索response
 */
@JsonClass(generateAdapter = true)
data class SearchTagListResp(
    val trend_tags: List<TrendTag> = listOf()
)

@JsonClass(generateAdapter = true)
data class TrendTag(
    val illust: Illust = Illust(),
    val tag: String = "",
    val translated_name: String = "",
    var type: Int = TYPE_NORMAL
): MultiItemEntity {
    companion object {
        const val TYPE_NORMAL = 0
        const val TYPE_HEADER = 1
    }
    override fun getItemType(): Int = type
}

@JsonClass(generateAdapter = true)
data class TagListResp(val tags: List<Tag>)
