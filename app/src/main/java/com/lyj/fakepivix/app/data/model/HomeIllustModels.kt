package com.lyj.fakepivix.app.data.model

import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.model.response.IllustListResp
import com.lyj.fakepivix.app.data.model.response.Live
import com.lyj.fakepivix.app.data.model.response.SpotlightArticle

/**
 * @author greensun
 *
 * @date 2019/4/17
 *
 * @desc
 */

data class RecommendGroup (val lives: List<Live>, val illustListResp: IllustListResp)

//class SpotLight(val articles: List<SpotlightArticle>): Illust() {
//    override fun getItemType(): Int = TYPE_PIXIVISION
//}