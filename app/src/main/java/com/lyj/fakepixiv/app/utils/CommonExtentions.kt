package com.lyj.fakepixiv.app.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.application.ApplicationLike
import com.lyj.fakepixiv.app.databinding.onPropertyChangedCallback
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.widget.StateView


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



/**
 * 监听列表数据加载状态
 * [small] 小尺寸错误布局
 */
fun BaseQuickAdapter<*, *>.bindState(loadState: ObservableField<LoadState>,
                                     @LayoutRes loadingRes: Int = R.layout.layout_common_loading,
                                     @LayoutRes errorRes: Int = R.layout.layout_error,
                                     onSucceed: (() -> Unit)? = null, onFailed: ((err: Throwable) -> Unit)? = null,
                                     onLoading: (() -> Unit)? = null, refreshLayout: SwipeRefreshLayout? = null,
                                     reload: (() -> Unit)? = null) {
    var firstLoad = true

    val stateView = StateView(ApplicationLike.context)
    stateView.reload = {
        reload?.invoke()
    }
    stateView.loadState = loadState
    stateView.loadingRes = loadingRes
    stateView.errorRes = errorRes
    emptyView = stateView
    refreshLayout?.let {
        it.setOnRefreshListener {
            reload?.invoke()
        }
    }
    loadState.addOnPropertyChangedCallback(onPropertyChangedCallback { observable, i ->
        when (val state = loadState.get()) {
            is LoadState.Loading -> {
                if (headerLayoutCount + footerLayoutCount > 0) {
                    notifyDataSetChanged()
                 }
                //refreshLayout?.isRefreshing = false
                if (firstLoad) {
                    // 是第一次加载
                    refreshLayout?.isEnabled = false
                }
                onLoading?.invoke()
            }
            is LoadState.Failed -> {
                if (headerLayoutCount + footerLayoutCount > 0) {
                    notifyDataSetChanged()
                }
                refreshLayout?.isRefreshing = false
                refreshLayout?.isEnabled = false
                onFailed?.invoke(state.error)
            }
            else -> {
                firstLoad = false
                refreshLayout?.isRefreshing = false
                refreshLayout?.isEnabled = true
                onSucceed?.invoke()
            }
        }
    })
}


