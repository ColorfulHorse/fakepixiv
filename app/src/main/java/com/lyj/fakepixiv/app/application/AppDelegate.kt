package com.lyj.fakepixiv.app.application

import android.app.Application
import android.content.Intent
import com.lyj.fakepixiv.BuildConfig
import com.lyj.fakepixiv.app.service.WorkerService
import com.lyj.fakepixiv.app.tinker.TinkerManager
import com.lyj.fakepixiv.app.utils.AppManager
import com.tencent.tinker.lib.tinker.Tinker
import com.tencent.tinker.lib.tinker.TinkerApplicationHelper
import com.tencent.tinker.lib.tinker.TinkerInstaller
import me.yokeyword.fragmentation.Fragmentation
import timber.log.Timber

/**
 * @author greensun
 *
 * @date 2019/6/28
 *
 * @desc
 */
class AppDelegate {
    companion object {
        lateinit var application: Application
    }

    fun onCreate(app: Application) {
        application = app
        Fragmentation.builder()
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(BuildConfig.DEBUG)
                .install()

        app.registerActivityLifecycleCallbacks(AppManager.instance)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        app.startService(Intent(app, WorkerService::class.java))
    }

    fun onDestroy() {
        application.stopService(Intent(application, WorkerService::class.java))
    }
}