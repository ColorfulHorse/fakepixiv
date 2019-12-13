package com.lyj.fakepixiv.app.utils

import java.text.ParseException
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
    private val local: SimpleDateFormat by lazy { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) }
    /**
     *
     */
    @JvmStatic fun isNew(dateStr: String): Boolean {
        val date = sdf.parse(dateStr)
        val current = Date()
        val distance = current.time - date.time
        return distance < dayTime
    }

    @JvmStatic fun formatHistory(timestamp: Long): String {
        if (timestamp > 0) {
            // 今天0点
            val t = Calendar.getInstance()
            t.set(Calendar.MILLISECOND, 0)
            t.set(Calendar.SECOND, 0)
            t.set(Calendar.MINUTE, 0)
            t.set(Calendar.HOUR_OF_DAY, 0)
            // 昨天0点
            val y = Calendar.getInstance()
            y.timeInMillis = t.timeInMillis
            y.add(Calendar.DAY_OF_MONTH, -1)
            val last = Calendar.getInstance()
            last.timeInMillis = timestamp
            var sdf = SimpleDateFormat("YYYY-MM-dd HH:mm", Locale.getDefault())
            if (last.after(t)){
                // 今天
                sdf = SimpleDateFormat("今天 HH:mm", Locale.getDefault())
            }else if (last.before(t) and last.after(y)) {
                // 昨天
                sdf = SimpleDateFormat("昨天 HH:mm", Locale.getDefault())
            }else if (last.get(Calendar.YEAR) == t.get(Calendar.YEAR)) {
                sdf = SimpleDateFormat("MM-dd HH:mm", Locale.getDefault())
            }
            return sdf.format(timestamp)
        }
        return ""
    }

    /**
     *
     */
    @JvmStatic fun format(dateStr: String): String {
        return try {
            val date = sdf.parse(dateStr)
            local.format(date)
        }catch (e: ParseException) {
            dateStr
        }
    }

    @JvmStatic fun format(date: Date): String {
        return try {
            sdf.format(date)
        }catch (e: ParseException) {
            ""
        }
    }

}