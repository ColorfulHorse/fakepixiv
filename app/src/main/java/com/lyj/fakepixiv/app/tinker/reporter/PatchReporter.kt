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
import android.content.Intent
import com.lyj.fakepixiv.app.constant.Constant

import com.lyj.fakepixiv.app.utils.ToastUtil
import com.tencent.tinker.lib.reporter.DefaultPatchReporter
import com.tencent.tinker.lib.tinker.TinkerInstaller
import com.tencent.tinker.loader.shareutil.SharePatchInfo

import java.io.File

/**
 * Tinker在修复或者升级补丁时的一些回调
 */
class PatchReporter(context: Context) : DefaultPatchReporter(context) {

    override fun onPatchServiceStart(intent: Intent) {
        super.onPatchServiceStart(intent)
        TinkerReport.onApplyPatchServiceStart()
    }

    /**
     * 对合成的dex文件提前进行dexopt时出现异常，默认我们会删除临时文件。
     * @param patchFile
     * @param dexFiles
     * @param t
     */
    override fun onPatchDexOptFail(patchFile: File, dexFiles: List<File>, t: Throwable) {
        super.onPatchDexOptFail(patchFile, dexFiles, t)
        TinkerReport.onApplyDexOptFail(t)
    }

    /**
     * 在补丁合成过程捕捉到异常，十分希望你可以把错误信息反馈给我们。默认我们会删除临时文件，并且将tinkerFlag设为不可用。
     * @param patchFile
     * @param e
     */
    override fun onPatchException(patchFile: File, e: Throwable) {
        super.onPatchException(patchFile, e)
        TinkerReport.onApplyCrash(e)
    }

    /**
     * patch.info是用来管理补丁包版本的文件，这是在更新info文件时发生损坏的回调。默认我们会卸载补丁包，因为此时我们已经无法恢复了。
     * @param patchFile
     * @param oldVersion
     * @param newVersion
     */
    override fun onPatchInfoCorrupted(patchFile: File?, oldVersion: String, newVersion: String) {
        super.onPatchInfoCorrupted(patchFile, oldVersion, newVersion)
        TinkerReport.onApplyInfoCorrupted()
    }

    /**
     * 补丁合成过程对输入补丁包的检查失败，这里可以通过错误码区分，例如签名校验失败、tinkerId不一致等原因。默认我们会删除临时文件。
     * @param patchFile
     * @param errorCode
     */
    override fun onPatchPackageCheckFail(patchFile: File, errorCode: Int) {
        super.onPatchPackageCheckFail(patchFile, errorCode)
        TinkerReport.onApplyPackageCheckFail(errorCode)
    }

    /**
     * 这个是无论补丁合成失败或者成功都会回调的接口，它返回了本次合成的类型，时间以及结果等。
     * 默认我们只是简单的输出这个信息，你可以在这里加上监控上报逻辑。
     * @param patchFile
     * @param success
     * @param cost
     */
    override fun onPatchResult(patchFile: File, success: Boolean, cost: Long) {
        super.onPatchResult(patchFile, success, cost)
        if (success) {
            ToastUtil.showToast("合成差异包成功")
            val patch = File(Constant.File.PATCH_PATH)
            if (patch.exists()) {
                patch.delete()
            }
        }
        TinkerReport.onApplied(cost, success)
    }

    /**
     * 从补丁包与原始安装包中合成某种类型的文件出现错误，默认我们会删除临时文件。
     * @param patchFile
     * @param extractTo
     * @param filename
     * @param fileType
     */
    override fun onPatchTypeExtractFail(patchFile: File, extractTo: File, filename: String, fileType: Int) {
        super.onPatchTypeExtractFail(patchFile, extractTo, filename, fileType)
        TinkerReport.onApplyExtractFail(fileType)
    }

    /**
     * 对patch.info的校验版本合法性校验。若校验失败，默认我们会删除临时文件。
     * @param patchFile
     * @param oldPatchInfo
     * @param patchFileVersion
     */
    override fun onPatchVersionCheckFail(patchFile: File, oldPatchInfo: SharePatchInfo?, patchFileVersion: String) {
        super.onPatchVersionCheckFail(patchFile, oldPatchInfo, patchFileVersion)
        TinkerReport.onApplyVersionCheckFail()
    }

    companion object {
        private val TAG = "Tinker.SamplePatchReporter"
    }
}
