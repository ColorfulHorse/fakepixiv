package com.lyj.fakepixiv.app

import android.app.Application
import android.content.Context
import com.lyj.fakepixiv.BuildConfig
import com.lyj.fakepixiv.app.lifecycle.AppDelegate
import me.yokeyword.fragmentation.Fragmentation

/**
 * @author greensun
 *
 * @date 2019/3/20
 *
 * @desc
 */
class App : Application() {
    companion object {
        lateinit var context: Context
    }

    val appDelegate = AppDelegate()

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        Fragmentation.builder()
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(BuildConfig.DEBUG)
                .install()
        appDelegate.onCreate(this)
    }
}