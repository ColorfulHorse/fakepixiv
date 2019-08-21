package com.lyj.fakepivix.app.databinding

import android.databinding.BindingAdapter
import android.databinding.BindingConversion
import android.databinding.BindingMethod
import android.graphics.drawable.Drawable
import android.support.constraint.ConstraintLayout
import android.support.design.widget.FloatingActionButton
import android.text.Html
import android.transition.Fade
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.lyj.fakepivix.GlideApp
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.app.utils.dp2px
import com.lyj.fakepivix.widget.LikeButton

/**
 * @author greensun
 *
 * @date 2019/3/23
 *
 * @desc
 */

val factory: DrawableCrossFadeFactory = DrawableCrossFadeFactory.Builder(400).setCrossFadeEnabled(true).build()

@BindingAdapter(value = ["url", "placeHolder", "placeHolderRatio",  "error", "circle", "fade", "blur"], requireAll = false)
fun ImageView.url(url: String?, placeHolder: Drawable?, placeHolderRatio: String?, error: Drawable?, circle: Boolean = false, fade: Boolean = false, blur: Boolean) {
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
                    if (placeHolderRatio == null) {
                        options.placeholder(placeHolder)
                    }else {
                        var thumbnail = GlideApp.with(this)
                                .asDrawable()
                                .load(placeHolder)
                                .override(width, 200.dp2px())
//                        val base = placeHolderRatio.split(",")[0]
//                        val ratio = placeHolderRatio.split(",")[1]
//                        var baseW = false
//                        if (base.equals("W", ignoreCase = true)) {
//                            baseW = true
//                        }
//                        val res = ratio.trim().split(":")
//                        val w = if (baseW) res[0].toInt() else res[1].toInt()
//                        val h = if (baseW) res[1].toInt() else res[0].toInt()
//                        if (w <=0 || h<=0) {
//                            thumbnail = if (baseW) {
//                                thumbnail.override(width, 200.dp2px())
//                            }else {
//                                thumbnail.override(200.dp2px(), height)
//                            }
//                        }else {
//                            thumbnail = if (baseW) {
//                                val newHeight = width*1f*h/w
//                                thumbnail.override(width, newHeight.toInt())
//                            }else {
//                                val newWidth = height*1f*w/h
//                                thumbnail.override(newWidth.toInt(), height)
//                            }
//                        }

                        req = req.thumbnail(thumbnail)
                    }
                    //options.placeholder(placeHolder)
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

@BindingAdapter(value = ["show"])
fun FloatingActionButton.show(show: Boolean = true) {
    if (show) this.show() else this.hide()
}

@BindingAdapter(value = ["fadeIn"])
fun View.fadeIn(show: Boolean = true) {
    var anim = this.animate().alpha(1f)
    if (!show) {
        anim = this.animate().alpha(0f)
    }
    anim.duration = 200
    anim.start()
}

@BindingAdapter(value = ["dimensionRatio"])
fun View.dimensionRatio(dimensionRatio: String) {
    val res = dimensionRatio.split(",")[1].trim().split(":")
    val x = res[0].toInt()
    val y = res[1].toInt()
    val lp = this.layoutParams as ConstraintLayout.LayoutParams
    if (x <= 0 || y <= 0) {
        lp.constrainedHeight = false
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT
    }else {
        lp.dimensionRatio = dimensionRatio
    }
    this.layoutParams = lp
}


@BindingConversion
fun stateToVisibility(loadState: LoadState) = if (loadState is LoadState.Loading) View.VISIBLE else View.GONE
