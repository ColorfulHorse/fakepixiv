package com.lyj.fakepivix.app.utils

import com.squareup.moshi.ToJson
import com.squareup.moshi.FromJson
import javax.annotation.Nullable


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
