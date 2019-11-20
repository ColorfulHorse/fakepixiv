package com.lyj.fakepixiv.app.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import com.lyj.fakepixiv.app.application.ApplicationLike
import java.io.File

/**
 * @author green sun
 *
 * @date 2019/11/11
 *
 * @desc
 */
object FileUtil {

    const val PICTURE_ORIGINAL = "original"

    fun saveBitmap(bitmap: Bitmap, key: String) {
        var dir = ApplicationLike.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (dir == null) {
            dir = File("${ApplicationLike.context.filesDir.absolutePath}${File.separator}${Environment.DIRECTORY_PICTURES}")
        }
        dir = File(dir, PICTURE_ORIGINAL)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val fileName = "illust_${key}_${System.currentTimeMillis()}"
        val target = File(dir, fileName)
        target.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
    }
}