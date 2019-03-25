package com.lyj.fakepivix.app.utils

import android.content.Context
import com.bumptech.glide.load.model.GlideUrl

/**
 * @author greensun
 *
 * @date 2019/3/23
 *
 * @desc
 */
fun Context.px2dp(value: Float): Int {
    val scale =  resources.displayMetrics.density
    return (value/scale+0.5f).toInt()
}

fun Context.dp2px(value: Float): Int {
    val scale =  resources.displayMetrics.density
    return (value*scale+0.5f).toInt()
}