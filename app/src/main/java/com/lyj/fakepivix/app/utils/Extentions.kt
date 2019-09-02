package com.lyj.fakepivix.app.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.databinding.ObservableField
import android.support.annotation.ColorRes
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Half.toFloat
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.bumptech.glide.load.model.GlideUrl
import android.view.WindowManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.App
import com.lyj.fakepivix.app.databinding.onPropertyChangedCallback
import com.lyj.fakepivix.app.network.LoadState
import kotlinx.android.synthetic.main.layout_common_refresh_recycler.*
import kotlinx.android.synthetic.main.layout_error.view.*


/**
 * @author greensun
 *
 * @date 2019/3/23
 *
 * @desc
 */
fun Number.px2dp(): Int {
    val scale =  App.context.resources.displayMetrics.density
    return (toFloat()/scale+0.5f).toInt()
}

fun Number.dp2px(): Int {
    val scale =  App.context.resources.displayMetrics.density
    return (toFloat()*scale+0.5f).toInt()
}

fun screenWidth(): Int {
    return App.context.resources.displayMetrics.widthPixels
}

fun screenHeight(): Int {
    return App.context.resources.displayMetrics.heightPixels
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

fun Fragment.finish() {
    this.activity?.finish()
}

fun Context.hideKeyboard() {
    val imm = this.getSystemService (Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (imm.isActive) {
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}


/**
 * 监听列表数据加载状态
 * [small] 小尺寸错误布局
 */
fun BaseQuickAdapter<*, *>.bindState(loadState: ObservableField<LoadState>, onSucceed: (() -> Unit)? = null, onFailed: ((err: Throwable) -> Unit)? = null,
                                     onLoading: (() -> Unit)? = null, refreshLayout: SwipeRefreshLayout? = null, small: Boolean = false, reload: (() -> Unit)? = null) {
    val loadingView: View = LayoutInflater.from(AppManager.instance.top).inflate(R.layout.layout_common_loading, null)
    val errorView: View =
            if (small) LayoutInflater.from(AppManager.instance.top).inflate(R.layout.layout_error_small, null)
            else LayoutInflater.from(AppManager.instance.top).inflate(R.layout.layout_error, null)

    emptyView = loadingView
    errorView.reload.setOnClickListener {
        reload?.invoke()
    }
    refreshLayout?.let {
        it.setOnRefreshListener {
            reload?.invoke()
        }
    }
    loadState.addOnPropertyChangedCallback(onPropertyChangedCallback { observable, i ->
        when (loadState.get()) {
            is LoadState.Loading -> {
                emptyView = loadingView
                if (headerLayoutCount + footerLayoutCount > 0) {
                    notifyDataSetChanged()
                }
                refreshLayout?.isRefreshing = false
                refreshLayout?.isEnabled = false
                onLoading?.invoke()
            }
            is LoadState.Failed -> {
                emptyView = errorView
                if (headerLayoutCount + footerLayoutCount > 0) {
                    notifyDataSetChanged()
                }
                refreshLayout?.isEnabled = true
                onFailed?.invoke((loadState.get() as LoadState.Failed).error)
            }
            else -> {
                refreshLayout?.isEnabled = true
                onSucceed?.invoke()
            }
        }
    })
}

