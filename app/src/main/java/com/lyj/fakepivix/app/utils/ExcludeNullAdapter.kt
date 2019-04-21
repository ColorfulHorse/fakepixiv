package com.lyj.fakepivix.app.utils

import com.squareup.moshi.ToJson
import com.squareup.moshi.FromJson
import javax.annotation.Nullable


/**
 * @author greensun
 *
 * @date 2019/4/20
 *
 * @desc 把json中所有null替换成空Object
 */
class ExcludeNullAdapter {
    @FromJson
    fun fromJson(@Nullable value: Any?): Any {
        return value?: Any()
    }

    @ToJson
    fun toJson(@Nullable value: Any?): Any {
        return value?: Any()
    }
}
