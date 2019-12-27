package com.lyj.fakepixiv.widget

import android.text.style.ImageSpan

/**
 * @author green sun
 *
 * @date 2019/12/26
 *
 * @desc
 */
class EmojiSpan(val original: String, drawable: URLDrawable, alignment: Int) : ImageSpan(drawable, alignment) {

    override fun toString(): String {
        return original
    }
}