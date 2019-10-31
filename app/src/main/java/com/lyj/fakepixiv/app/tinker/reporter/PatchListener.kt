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

package com.lyj.fakepixiv.app.tinker.reporter

import android.app.ActivityManager
import android.content.Context

import com.lyj.fakepixiv.app.tinker.TinkerExceptionHandler
import com.lyj.fakepixiv.app.tinker.Utils
import com.tencent.tinker.lib.listener.DefaultPatchListener
import com.tencent.tinker.lib.util.TinkerLog
import com.tencent.tinker.loader.shareutil.ShareConstants
import com.tencent.tinker.loader.shareutil.SharePatchFileUtil
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals

import java.io.File


/**
 * 过滤Tinker收到的补丁包的修复、升级请求，也就是决定我们是不是真的要唤起:patch进程去尝试补丁合成。
 */
class PatchListener(context: Context) : DefaultPatchListener(context) {

    private val maxMemory: Int = (context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).memoryClass

    init {
        TinkerLog.i(TAG, "application maxMemory:$maxMemory")
    }

    /**
     * 若检查成功，我们会调用TinkerPatchService.runPatchService唤起:patch进程，去尝试完成补丁合成操作。
     * 反之，会回调检验失败的接口。事实上，你只需要复写patchCheck函数即可。
     * 若检查失败，会在LoadReporter的onLoadPatchListenerReceiveFail中回调。
     */
    public override fun patchCheck(path: String, patchMd5: String): Int {
        val patchFile = File(path)
        TinkerLog.i(TAG, "receive a patch file: %s, file size:%d", path, SharePatchFileUtil.getFileOrDirectorySize(patchFile))
        var returnCode = super.patchCheck(path, patchMd5)

        if (returnCode == ShareConstants.ERROR_PATCH_OK) {
            returnCode = Utils.checkForPatchRecover(NEW_PATCH_RESTRICTION_SPACE_SIZE_MIN, maxMemory)
        }

        if (returnCode == ShareConstants.ERROR_PATCH_OK) {
            val sp = context.getSharedPreferences(ShareConstants.TINKER_SHARE_PREFERENCE_CONFIG, Context.MODE_MULTI_PROCESS)
            //optional, only disable this patch file with md5
            val fastCrashCount = sp.getInt(patchMd5, 0)
            if (fastCrashCount >= TinkerExceptionHandler.MAX_CRASH_COUNT) {
                returnCode = Utils.ERROR_PATCH_CRASH_LIMIT
            }
        }
        // Warning, it is just a sample case, you don't need to copy all of these
        // Interception some of the request
        if (returnCode == ShareConstants.ERROR_PATCH_OK) {
            val properties = ShareTinkerInternals.fastGetPatchPackageMeta(patchFile)
            if (properties == null) {
                returnCode = Utils.ERROR_PATCH_CONDITION_NOT_SATISFIED
            } else {
                val platform = properties.getProperty(Utils.PLATFORM)
                TinkerLog.i(TAG, "get platform:$platform")
                // check patch platform require
                //                if (platform == null || !platform.equals(BuildInfo.PLATFORM)) {
                //                    returnCode = Utils.ERROR_PATCH_CONDITION_NOT_SATISFIED;
                //                }
            }
        }

        TinkerReport.onTryApply(returnCode == ShareConstants.ERROR_PATCH_OK)
        return returnCode
    }

    companion object {
        private val TAG = "Tinker.SamplePatchListener"

        protected val NEW_PATCH_RESTRICTION_SPACE_SIZE_MIN = (60 * 1024 * 1024).toLong()
    }
}
