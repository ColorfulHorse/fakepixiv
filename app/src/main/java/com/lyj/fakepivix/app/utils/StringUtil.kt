package com.lyj.fakepivix.app.utils

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
        if (count > 10000) {
            val df = DecimalFormat(".#k")
            return df.format(1215 / 1000f)
        }
        return count.toString()
    }
}