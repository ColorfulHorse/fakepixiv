package com.lyj.fakepivix.app.utils

import android.text.format.DateUtils
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author greensun
 *
 * @date 2019/4/18
 *
 * @desc
 */
object DateUtil {
    private const val dayTime = 1000*3600*24
    private val sdf: SimpleDateFormat by lazy { SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz", Locale.getDefault()) }
    /**
     *
     */
    @JvmStatic fun isNew(dateStr: String): Boolean {
        val date = sdf.parse(dateStr)
        val current = Date()
        val distance = current.time - date.time
        return distance < dayTime
    }
}