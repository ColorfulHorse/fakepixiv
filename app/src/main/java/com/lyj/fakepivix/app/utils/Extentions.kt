package com.lyj.fakepivix.app.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import android.view.inputmethod.InputMethodManager
import com.bumptech.glide.load.model.GlideUrl
import android.view.WindowManager



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

fun Activity.statusBarColor(color: Int) {
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    //设置状态栏颜色
    window.statusBarColor = color
}

fun <T> Context.startActivity(cls: Class<T>) {
    startActivity(Intent(this, cls))
}

fun Context.hideKeyboard() {
    val imm = this.getSystemService (Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (imm.isActive) {
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}
