package com.lyj.fakepixiv.app.utils

import android.app.Activity
import android.graphics.Rect
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.view.doOnLayout
import androidx.core.view.doOnPreDraw
import androidx.databinding.BindingAdapter
import com.lyj.fakepixiv.GlideApp
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.application.ApplicationLike.Companion.context
import com.lyj.fakepixiv.app.databinding.KeyboardListener
import com.lyj.fakepixiv.app.databinding.onPropertyChangedCallback
import com.lyj.fakepixiv.app.network.LoadState
import timber.log.Timber

/**
 * @author greensun
 *
 * @date 2019/4/19
 *
 * @desc
 */

inline fun View.doOnAttached(crossinline action: () -> Unit) {
    addOnAttachStateChangeListener(object: View.OnAttachStateChangeListener {
        override fun onViewDetachedFromWindow(v: View?) {

        }

        override fun onViewAttachedToWindow(v: View?) {
            removeOnAttachStateChangeListener(this)
            action()
        }

    })
}

inline fun View.doOnDetached(crossinline action: () -> Unit) {
    addOnAttachStateChangeListener(object: View.OnAttachStateChangeListener {
        override fun onViewDetachedFromWindow(v: View?) {
            removeOnAttachStateChangeListener(this)
            action()
        }

        override fun onViewAttachedToWindow(v: View?) {

        }

    })
}

fun View.onKeyboardChanged(action : (Boolean) -> Unit) {
    val root = rootView
    var rootViewHeight = -1

    val callback = object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            val rect = Rect()
            root.getWindowVisibleDisplayFrame(rect)
            if (rootViewHeight == -1) {
                rootViewHeight = rect.height()
                return
            }

            // 没有改变
            if (rootViewHeight == rect.height()) {
                return
            }

            if (rootViewHeight - rect.height() > 200) {
                if (getLocalVisibleRect(Rect())) {
                    action(true)
                }
                rootViewHeight = rect.height()
                return
            }

            if (rect.height() - rootViewHeight > 200) {
                if (getLocalVisibleRect(Rect())) {
                    action(false)
                }
                rootViewHeight = rect.height()
            }
        }
    }
   // doOnAttached {
        root.viewTreeObserver.addOnGlobalLayoutListener(callback)
    //}
    doOnDetached {
        root.viewTreeObserver.removeOnGlobalLayoutListener(callback)
    }
}

