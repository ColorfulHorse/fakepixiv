package com.lyj.fakepivix.app.databinding

import android.databinding.BindingAdapter
import android.databinding.BindingConversion
import android.graphics.drawable.Drawable
import android.text.Html
import android.transition.Fade
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.lyj.fakepivix.GlideApp
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.widget.LikeButton

/**
 * @author greensun
 *
 * @date 2019/3/23
 *
 * @desc
 */

val factory: DrawableCrossFadeFactory = DrawableCrossFadeFactory.Builder(400).setCrossFadeEnabled(true).build()

@BindingAdapter(value = ["url", "placeHolder", "error", "circle", "fade", "blur"], requireAll = false)
fun ImageView.url(url: String?, placeHolder: Drawable?, error: Drawable?, circle: Boolean = false, fade: Boolean = false, blur: Boolean) {
    url?.let {
        if (url.isNotEmpty()) {
            var req = GlideApp.with(this)
                    .load(url)
            if (fade) {
                req = req.transition(DrawableTransitionOptions.withCrossFade(factory))
            }
            val options = RequestOptions()
            if (circle) {
                options.circleCrop()
            }
            placeHolder?.let {
                if (circle) {
                    val thumbnail = GlideApp.with(this)
                            .load(placeHolder)
                            .circleCrop()
                    req = req.thumbnail(thumbnail)
                }else {
                    options.placeholder(placeHolder)
                }
            }
            error?.let {
                if (circle) {
                    req = req.error(GlideApp.with(this)
                            .load(error)
                            .circleCrop())
                }else {
                    options.error(error)
                }
            }
//            if (blur) {
//                options.transform()
//            }
            req.apply(options).into(this)
        }
    }
}

@BindingAdapter(value = ["liked"])
fun LikeButton.liked(liked: Boolean = false) {
    this.setLikedWithoutAmin(liked)
}

@BindingConversion
fun boolToVisibility(visibility: Boolean) = if (visibility) View.VISIBLE else View.GONE

@BindingAdapter(value = ["show"])
fun View.show(show: Boolean = true) {
    this.visibility = if (show) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter(value = ["html"])
fun TextView.html(content: String) {
    this.text = Html.fromHtml(content)
}

@BindingConversion
fun stateToVisibility(loadState: LoadState) = if (loadState is LoadState.Loading) View.VISIBLE else View.GONE
