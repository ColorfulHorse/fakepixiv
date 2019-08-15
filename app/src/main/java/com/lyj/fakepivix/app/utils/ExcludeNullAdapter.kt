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
class ExcludeNullAdapter {
    @FromJson
    fun fromJson(@Nullable value: String?): String {
        return value?:""
    }

    @ToJson
    fun toJson(@Nullable value: String?): String {
        return value?: ""
    }
}
