package com.lyj.fakepivix.app.utils

import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.App
import com.lyj.fakepivix.app.data.model.response.Illust
import java.text.DecimalFormat

/**
 * @author greensun
 *
 * @date 2019/4/27
 *
 * @desc
 */
object StringUtil {

    @JvmStatic
    fun formatLength(length: Int): String {
        val df = DecimalFormat(",###")
        return df.format(length)
    }

    @JvmStatic
    fun formatCount(count: Int): String {
        if (count > 1000) {
            val df = DecimalFormat(".#k")
            return df.format(count / 1000f)
        }
        return count.toString()
    }

    @JvmStatic
    fun transformTextLength(count: Int): String {
        // 每分钟500字
        val min = count / 500
        if (min == 0) {
            return "少于一分钟"
        }
        val sb = StringBuilder()
        var h = 0
        var m = min
        if (min >= 60) {
            h = min/60
            m = min%60
        }
        if (h > 0) {
            sb.append(h).append("小时")
        }
        sb.append(m).append("分钟")
        return sb.toString()
    }

}