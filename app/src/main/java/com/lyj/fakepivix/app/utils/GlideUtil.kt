package com.lyj.fakepivix.app.utils

import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.Headers
import java.util.HashMap

/**
 * @author greensun
 *
 * @date 2019/3/23
 *
 * @desc
 */
object GlideUtil {
    private const val MAP_KEY = "Referer"
    private const val IMAGE_REFERER = "https://app-api.pixiv.net/"
    val HEADERS = {mapOf(Pair(MAP_KEY, IMAGE_REFERER))}
}

/**
 * 转换图片url
 */
fun String.mapUrl() : GlideUrl = GlideUrl(this, GlideUtil.HEADERS)
