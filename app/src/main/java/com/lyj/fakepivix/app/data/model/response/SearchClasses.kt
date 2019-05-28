package com.lyj.fakepivix.app.data.model.response

/**
 * @author greensun
 *
 * @date 2019/5/28
 *
 * @desc 搜索response
 */

data class SearchTagListResp(
    val trend_tags: List<TrendTag> = listOf()
)

data class TrendTag(
    val illust: Illust = Illust(),
    val tag: String = "",
    val translated_name: String = ""
)
