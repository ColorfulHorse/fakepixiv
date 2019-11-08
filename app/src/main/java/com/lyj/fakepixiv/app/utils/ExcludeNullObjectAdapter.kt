package com.lyj.fakepixiv.app.utils

import androidx.annotation.Nullable
import com.squareup.moshi.FromJson


/**
 * @author greensun
 *
 * @date 2019/4/20
 *
 * @desc 把json中所有null替换成""
 */
class ExcludeNullObjectAdapter {
    @FromJson
    fun fromJson(@Nullable value: Any): Any? {
        if (value.toString() == "{}") {
            return null
        }
        return value
    }
}
