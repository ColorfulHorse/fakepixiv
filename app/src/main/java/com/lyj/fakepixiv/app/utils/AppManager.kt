package com.lyj.fakepixiv.app.utils

import android.app.Activity
import android.app.Application
import android.graphics.Rect
import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.fragment.app.FragmentActivity
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.BaseFragment
import com.lyj.fakepixiv.app.base.ViewModelProvider
import com.lyj.fakepixiv.app.constant.Constant
import com.lyj.fakepixiv.app.lifecycle.FragmentLifecycle

/**
 * @author greensun
 *
 * @date 2019/6/28
 *
 * @desc
 */
class AppManager : Application.ActivityLifecycleCallbacks {
    companion object {
        val instance: AppManager by lazy {
            AppManager()
        }
    }

    private val activityList = arrayListOf<Activity>()
    val top: Activity?
        get() {
            if (activityList.isNotEmpty()) {
                return activityList[activityList.size - 1]
            }
            return null
        }
    var current: Activity? = null
        private set(value) {
            field = value
        }

    override fun onActivityPaused(activity: Activity) {
        if (activity == current) {
            current = null
        }
    }

    override fun onActivityResumed(activity: Activity) {
        current = activity
    }

    override fun onActivityStarted(activity: Activity) {
        calKeyboardHeight(activity)
    }

    private fun calKeyboardHeight(activity: Activity) {
        val keyboardHeight = SPUtil.get(Constant.SP.KEY_KEYBOARD_HEIGHT)
        if (keyboardHeight == -1) {
            var rootViewHeight = -1
            val root = activity.window.decorView
            root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val origin = SPUtil.get(Constant.SP.KEY_KEYBOARD_HEIGHT)
                    // 已经测量过了
                    if (origin != -1) {
                        root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        return
                    }
                    val rect = Rect()
                    root.getWindowVisibleDisplayFrame(rect)
                    if (rootViewHeight == -1) {
                        rootViewHeight = rect.height()
                        return
                    }

                    if (rootViewHeight == rect.height()) {
                        return
                    }

                    val offset = rootViewHeight - rect.height()
                    if (offset > 200) {
                        SPUtil.save(Constant.SP.KEY_KEYBOARD_HEIGHT, offset)
                        root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }
            })
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        activityList.remove(activity)
        if (activity is BaseFragment<*, *>) {
            activity.mViewModel?.let {
                //ViewModelProvider - it.hashCode()
            }
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {

    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activityList.add(activity)
        if (activity is FragmentActivity) {
            if (activity is BaseFragment<*, *>) {
                activity.mViewModel?.let {
                    //ViewModelProvider[activity.hashCode()] = it
                }
            }
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(FragmentLifecycle, true)
        }
    }

}