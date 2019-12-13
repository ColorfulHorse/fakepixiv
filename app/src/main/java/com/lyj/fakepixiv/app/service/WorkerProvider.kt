package com.lyj.fakepixiv.app.service

import android.content.Context
import android.os.Binder
import com.lyj.fakepixiv.BuildConfig
import com.lyj.fakepixiv.app.application.ApplicationLike
import com.lyj.fakepixiv.app.constant.Constant
import com.lyj.fakepixiv.app.data.source.remote.CommonRepository
import com.tencent.tinker.lib.tinker.TinkerInstaller
import kotlinx.coroutines.*
import java.io.File

/**
 * @author green sun
 *
 * @date 2019/12/5
 *
 * @desc
 */
class WorkerProvider(val context: Context) : Binder() , CoroutineScope by CoroutineScope(Dispatchers.Main + SupervisorJob()){

    fun init() {
        checkPatch()
    }

    /**
     * 检查热更新
     */
    private fun checkPatch() {
        launch(CoroutineExceptionHandler { _, err ->

        }) {
            val path = withContext(Dispatchers.IO) {
                val version = BuildConfig.VERSION_CODE
                val patchVersion = Constant.Patch.version
                val service = CommonRepository.instance
                        .service
                val resp = service
                        .checkPatch(version, patchVersion)
                if (resp.hasPatch) {
                    val dir = File(ApplicationLike.context.externalCacheDir?: ApplicationLike.context.filesDir, "patch")
                    if (!dir.exists()) {
                        dir.mkdirs()
                    }
                    val file = File(dir, "patch_${version}_${patchVersion}.apk")
                    val stream = service
                            .downLoad(resp.url)
                            .byteStream()
                    stream.use {
                        val bis = it.buffered()
                        file.outputStream().use { os ->
                            val bos = os.buffered()
                            bis.copyTo(bos)
                        }
                    }
                    return@withContext file.absolutePath
                }
                null
            }
            path?.let {
                // 热更新用
                val patch = File(it)
                if (patch.exists()) {
                    TinkerInstaller.onReceiveUpgradePatch(context, path)
                }
            }
        }
    }

    fun destroy() {
        coroutineContext.cancelChildren()
    }
}