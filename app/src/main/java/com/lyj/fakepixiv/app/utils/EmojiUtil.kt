package com.lyj.fakepixiv.app.utils

import android.graphics.Paint
import android.text.Spannable
import android.text.SpannableString
import android.text.style.DynamicDrawableSpan
import android.text.style.ImageSpan
import android.widget.TextView
import com.lyj.fakepixiv.app.constant.Constant
import com.lyj.fakepixiv.app.data.model.response.EmojiResp
import com.lyj.fakepixiv.widget.EmojiSpan
import com.lyj.fakepixiv.widget.URLDrawable
import java.util.regex.Pattern

/**
 * @author green sun
 *
 * @date 2019/12/20
 *
 * @desc
 */
object EmojiUtil {

    val map = mutableMapOf<String, String>()

    fun `init`() {
        val emojiResp = SPUtil.getObj<EmojiResp>(Constant.SP.KEY_EMOJI)
        emojiResp?.let {
            it.emoji_definitions.forEach { emoji ->
                map[emoji.slug] = emoji.image_url_medium
            }
        }
    }

    fun richText(tv: TextView, content: String) {
        if (map.isNotEmpty()) {
            val ss = SpannableString(content)
            val pattern = Pattern.compile("\\([^\\(\\)]+\\)")
            val matcher = pattern.matcher(content)
            while (matcher.find()) {
                val item = matcher.group()
                val key = item.substring(1, item.length - 1)
                val url = map[key]
                if (url != null) {
                    val drawable = URLDrawable(url, tv)
                    val imgSpan = EmojiSpan(item, drawable, DynamicDrawableSpan.ALIGN_CENTER)
                    ss.setSpan(imgSpan, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
            tv.text = ss
        }
    }
}