package com.lyj.fakepixiv.app.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.databinding.BaseObservable
import androidx.databinding.Observable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.lyj.fakepixiv.app.application.ApplicationLike
import com.lyj.fakepixiv.app.databinding.LiveOnPropertyChangedCallback


/**
 * @author greensun
 *
 * @date 2019/3/23
 *
 * @desc
 */
fun Number.px2dp(): Int {
    val scale =  ApplicationLike.context.resources.displayMetrics.density
    return (toFloat()/scale+0.5f).toInt()
}

fun Number.dp2px(): Int {
    val scale =  ApplicationLike.context.resources.displayMetrics.density
    return (toFloat()*scale+0.5f).toInt()
}

fun screenWidth(): Int {
    return ApplicationLike.context.resources.displayMetrics.widthPixels
}

fun screenHeight(): Int {
    return ApplicationLike.context.resources.displayMetrics.heightPixels
}

fun Context.statusBarHeight(): Int {
    val result = 0
    try {
        val resourceId = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            val sizeOne = resources.getDimensionPixelSize(resourceId)
            val sizeTwo = Resources.getSystem().getDimensionPixelSize(resourceId)

            if (sizeTwo >= sizeOne) {
                return sizeTwo
            } else {
                val densityOne = resources.displayMetrics.density
                val densityTwo = Resources.getSystem().displayMetrics.density
                val f = sizeOne * densityTwo / densityOne
                return (if (f >= 0) f + 0.5f else f - 0.5f).toInt()
            }
        }
    } catch (ignored: Resources.NotFoundException) {
        return 0
    }

    return result
}

fun Activity.statusBarColor(color: Int) {
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    //设置状态栏颜色
    window.statusBarColor = color
}

fun Context.softInputActive(): Boolean {
    val imm = this.getSystemService (Context.INPUT_METHOD_SERVICE) as InputMethodManager
    return imm.isActive
}

fun Context.hideSoftInput() {
    val imm = this.getSystemService (Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (imm.isActive) {
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}

fun <T> Activity.startActivity(cls: Class<T>) {
    startActivity(Intent(this, cls))
}

fun Fragment.finish() {
    this.activity?.finish()
}

fun <T> Fragment.startActivity(cls: Class<T>) {
    this.startActivity(Intent(activity, cls))
}


inline fun BaseObservable.doOnPropertyChanged(lifecycle: Lifecycle? = null, crossinline consumer : (Observable, Int) -> Unit): LiveOnPropertyChangedCallback {
    val ob = LiveOnPropertyChangedCallback(this, lifecycle) { ob, id ->
        consumer(ob, id)
    }
    addOnPropertyChangedCallback(ob)
    return ob
}


