package com.lyj.fakepixiv.app.utils

import android.support.annotation.Nullable
import com.squareup.moshi.ToJson
import com.squareup.moshi.FromJson


/**
 * @author greensun
 *
 * @date 2019/4/20
 *
 * @desc 把json中所有null替换成""
 */
class ExcludeNullAdapter {
    @FromJson
    fun fromJson(@Nullable value: String?): String? {
        if (value == null) {
            return ""
        }
        return value
    }

    @ToJson
    fun toJson(@Nullable value: String?): String {
        return value?: ""
    }
}
