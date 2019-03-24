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

@BindingAdapter(value = ["url", "error"], requireAll = false)
fun ImageView.url(url: String, error: Drawable?) {
    var req = GlideApp.with(this)
            .load(url.mapUrl())
    error?.let {
        val options = RequestOptions()
                .error(error)
        req = req.apply(options)
    }
    req.into(this)
}

@BindingConversion
fun boolToVisiable(visibility: Boolean) = if (visibility) View.VISIBLE else View.GONE