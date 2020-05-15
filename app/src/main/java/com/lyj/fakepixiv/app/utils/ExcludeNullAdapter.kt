package com.lyj.fakepixiv.app.utils

import androidx.annotation.Nullable
import com.squareup.moshi.ToJson
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import timber.log.Timber


/**
 * @author greensun
 *
 * @date 2019/4/20
 *
 * @desc 把json中所有null替换成""
 */
class ExcludeNullAdapter {

    @FromJson fun fromJson(reader: JsonReader): String {
        val token = reader.peek()
        if (token != JsonReader.Token.NULL) {
            return reader.nextString()
        }
        reader.nextNull<Unit>()
        return ""
    }
}
