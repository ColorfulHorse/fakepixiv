package com.lyj.fakepivix.app

import android.app.Application
import com.lyj.fakepivix.BuildConfig
import me.yokeyword.fragmentation.Fragmentation

/**
 * @author greensun
 *
 * @date 2019/3/20
 *
 * @desc
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Fragmentation.builder()
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(BuildConfig.DEBUG)
                .install()
    }
}