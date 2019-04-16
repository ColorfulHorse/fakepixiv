package com.lyj.fakepivix.app.databinding

import android.databinding.BindingAdapter
import android.databinding.BindingConversion
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lyj.fakepivix.GlideApp
import com.lyj.fakepivix.app.utils.mapUrl

/**
 * @author greensun
 *
 * @date 2019/3/23
 *
 * @desc
 */

@BindingAdapter(value = ["url", "placeHolder", "error", "circle"], requireAll = false)
fun ImageView.url(url: String?, placeHolder: Drawable?, error: Drawable?, circle: Boolean = false) {

    url?.let {
        if (url.isNotEmpty()) {
            val req = GlideApp.with(this)
                    .load(url.mapUrl())
            val options = RequestOptions()
            placeHolder?.let {
                options.placeholder(placeHolder)
            }
            error?.let {
                options.error(error)
            }
            if (circle) {
                options.circleCrop()
            }
            req.apply(options).into(this)
        }
    }
}

@BindingConversion
fun boolToVisibility(visibility: Boolean) = if (visibility) View.VISIBLE else View.GONE