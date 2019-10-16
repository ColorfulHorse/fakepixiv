package com.lyj.fakepixiv.app.lifecycle

import android.app.Application
import com.lyj.fakepixiv.BuildConfig
import com.lyj.fakepixiv.app.utils.AppManager
import timber.log.Timber

/**
 * @author greensun
 *
 * @date 2019/6/28
 *
 * @desc
 */
class AppDelegate {
    lateinit var application: Application

    fun onCreate(app: Application) {
        application = app
        app.registerActivityLifecycleCallbacks(AppManager.instance)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}