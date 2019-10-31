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

import android.content.Context
import android.os.Looper

import com.tencent.tinker.lib.reporter.DefaultLoadReporter
import com.tencent.tinker.lib.util.UpgradePatchRetry
import com.tencent.tinker.loader.shareutil.ShareConstants

import java.io.File


/**
 * 加载补丁回调类
 */
class LoadReporter(context: Context) : DefaultLoadReporter(context) {

    /**
     * 所有的补丁合成请求都需要先通过PatchListener的检查过滤。
     * 这次检查不通过的回调，它运行在发起请求的进程。默认我们只是打印日志
     */
    override fun onLoadPatchListenerReceiveFail(patchFile: File, errorCode: Int) {
        super.onLoadPatchListenerReceiveFail(patchFile, errorCode)
        TinkerReport.onTryApplyFail(errorCode)
    }

    /**
     * 这个是无论加载失败或者成功都会回调的接口，它返回了本次加载所用的时间、返回码等信息。
     * 默认我们只是简单的输出这个信息，你可以在这里加上监控上报逻辑。
     */
    override fun onLoadResult(patchDirectory: File, loadCode: Int, cost: Long) {
        super.onLoadResult(patchDirectory, loadCode, cost)
        when (loadCode) {
            ShareConstants.ERROR_LOAD_OK -> TinkerReport.onLoaded(cost)
        }
        Looper.myQueue().addIdleHandler {
            if (UpgradePatchRetry.getInstance(context).onPatchRetryLoad()) {
                TinkerReport.onReportRetryPatch()
            }
            false
        }
    }

    override fun onLoadException(e: Throwable, errorCode: Int) {
        super.onLoadException(e, errorCode)
        TinkerReport.onLoadException(e, errorCode)
    }

    override fun onLoadFileMd5Mismatch(file: File, fileType: Int) {
        super.onLoadFileMd5Mismatch(file, fileType)
        TinkerReport.onLoadFileMisMatch(fileType)
    }

    /**
     * try to recover patch oat file
     *
     * @param file
     * @param fileType
     * @param isDirectory
     */
    override fun onLoadFileNotFound(file: File, fileType: Int, isDirectory: Boolean) {
        super.onLoadFileNotFound(file, fileType, isDirectory)
        TinkerReport.onLoadFileNotFound(fileType)
    }

    override fun onLoadPackageCheckFail(patchFile: File, errorCode: Int) {
        super.onLoadPackageCheckFail(patchFile, errorCode)
        TinkerReport.onLoadPackageCheckFail(errorCode)
    }

    override fun onLoadPatchInfoCorrupted(oldVersion: String, newVersion: String, patchInfoFile: File) {
        super.onLoadPatchInfoCorrupted(oldVersion, newVersion, patchInfoFile)
        TinkerReport.onLoadInfoCorrupted()
    }

    override fun onLoadInterpret(type: Int, e: Throwable) {
        super.onLoadInterpret(type, e)
        TinkerReport.onLoadInterpretReport(type, e)
    }

    /**
     * 补丁包版本升级的回调，只会在主进程调用。
     * 默认我们会杀掉其他所有的进程(保证所有进程代码的一致性)，并且删掉旧版本的补丁文件。
     */
    override fun onLoadPatchVersionChanged(oldVersion: String?, newVersion: String?, patchDirectoryFile: File, currentPatchName: String?) {
        super.onLoadPatchVersionChanged(oldVersion, newVersion, patchDirectoryFile, currentPatchName)
    }
}
