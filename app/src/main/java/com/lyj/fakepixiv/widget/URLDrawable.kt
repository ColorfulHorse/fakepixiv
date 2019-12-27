package com.lyj.fakepixiv.widget

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.style.ImageSpan
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.lyj.fakepixiv.GlideApp
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.utils.dp2px
import org.w3c.dom.Text


/**
 * @author green sun
 *
 * @date 2019/12/21
 *
 * @desc
 */
class URLDrawable(val url: String, val tv: TextView) : Drawable() {
    var drawable: Drawable? = null
    val placeHolder by lazy { ContextCompat.getDrawable(tv.context, R.drawable.placeholder_emoji) }
    val size = 24.dp2px()

    init {
        setBounds(0, 0, size, size)
    }

    override fun draw(canvas: Canvas) {
        if (drawable == null) {
            GlideApp
                    .with(tv.context)
                    .load(url)
                    .into(object : CustomTarget<Drawable>(size, size) {
                        override fun onLoadCleared(placeholder: Drawable?) {

                        }

                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            resource.setBounds(0, 0, size, size)
                            drawable = resource
                            tv.text = tv.text
                            if (tv is EditText) {
                                tv.setSelection(tv.text.length)
                            }
                        }
                    })
        }
        drawable?.draw(canvas)
    }

    override fun setAlpha(alpha: Int) {
        drawable?.alpha = alpha
    }

    override fun getOpacity(): Int {
        return drawable?.opacity?:0
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        drawable?.colorFilter = colorFilter
    }
}