/*
 * Tencent is pleased to support the open source community by making Tinker available.
 *
 * Copyright (C) 2016 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyj.fakepixiv.app.application

import android.annotation.TargetApi
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import com.lyj.fakepixiv.app.tinker.TinkerManager
import com.tencent.tinker.anno.DefaultLifeCycle
import com.tencent.tinker.entry.DefaultApplicationLike
import com.tencent.tinker.lib.tinker.Tinker
import com.tencent.tinker.loader.shareutil.ShareConstants


@DefaultLifeCycle(
        application = ".App",
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false)
class ApplicationLike(application: Application, tinkerFlags: Int, tinkerLoadVerifyFlag: Boolean,
                      applicationStartElapsedTime: Long, applicationStartMillisTime: Long, tinkerResultIntent: Intent) : DefaultApplicationLike(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent) {


    companion object {
        lateinit var context: Context
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    override fun onBaseContextAttached(base: Context?) {
        super.onBaseContextAttached(base)
        TinkerManager.setTinkerApplicationLike(this)
        context = application
        TinkerManager.initFastCrashProtect()
        TinkerManager.setUpgradeRetryEnable(true)
        TinkerManager.installTinker(this)
        val tinker = Tinker.with(application)
    }

    val appDelegate = AppDelegate()

    override fun onCreate() {
        super.onCreate()
        context = application
        appDelegate.onCreate(application)
    }

    override fun onTerminate() {
        super.onTerminate()
        appDelegate.onDestroy()
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    fun registerActivityLifecycleCallbacks(callback: Application.ActivityLifecycleCallbacks) {
        application.registerActivityLifecycleCallbacks(callback)
    }

}
