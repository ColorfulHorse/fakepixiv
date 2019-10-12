package com.lyj.fakepivix.app.lifecycle

import android.app.Application
import com.lyj.fakepivix.BuildConfig
import com.lyj.fakepivix.app.network.retrofit.RetrofitManager
import com.lyj.fakepivix.app.utils.AppManager
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