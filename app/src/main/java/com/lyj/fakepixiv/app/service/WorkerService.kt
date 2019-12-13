package com.lyj.fakepixiv.app.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * @author green sun
 *
 * @date 2019/12/5
 *
 * @desc
 */
class WorkerService : Service() {
    val provider = WorkerProvider(this)

    override fun onCreate() {
        super.onCreate()
        provider.init()
    }

    override fun onBind(intent: Intent): IBinder {
        return provider
    }

    override fun onDestroy() {
        provider.destroy()
        super.onDestroy()
    }
}