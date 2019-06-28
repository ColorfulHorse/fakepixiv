package com.lyj.fakepivix.app.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle

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

    }

    override fun onActivityDestroyed(activity: Activity) {
        activityList.remove(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {

    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activityList.add(activity)
    }

}