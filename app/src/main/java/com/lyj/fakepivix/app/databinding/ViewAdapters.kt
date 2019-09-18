package com.lyj.fakepivix.app.databinding

import android.databinding.BindingAdapter
import android.databinding.BindingConversion
import android.databinding.ObservableField
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.constraint.ConstraintLayout
import android.support.design.widget.FloatingActionButton
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.lyj.fakepivix.GlideApp
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.app.utils.dp2px
import com.lyj.fakepivix.widget.LikeButton
import android.graphics.drawable.BitmapDrawable
import android.graphics.Bitmap
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.glide.PositionedCrop
import com.lyj.fakepivix.app.utils.PaddingAnimationFactory
import com.lyj.fakepivix.app.utils.screenHeight
import com.lyj.fakepivix.app.utils.screenWidth
import jp.wasabeef.glide.transformations.BlurTransformation


/**
 * @author greensun
 *
 * @date 2019/3/23
 *
 * @desc
 */

val factory: DrawableCrossFadeFactory = DrawableCrossFadeFactory.Builder(400).setCrossFadeEnabled(true).build()

@BindingAdapter(value = ["url", "placeHolder", "placeHolderRatio", "topCrop", "fallback", "error", "circle", "fade", "blur"], requireAll = false)
fun ImageView.url(url: String?, placeHolder: Drawable?, placeHolderRatio: String?, topCrop: Boolean = false, error: Drawable?, fallback: Drawable?, circle: Boolean = false, fade: Boolean = false, blur: Boolean) {
    var req = GlideApp.with(this)
            .load(url)
    if (fade) {
        if (placeHolderRatio != null) {
            req = req.transition(DrawableTransitionOptions.with(PaddingAnimationFactory<Drawable>(factory)))
        }else {
            req = req.transition(DrawableTransitionOptions.withCrossFade(factory))
        }
    }
    val options = RequestOptions()
    if (circle) {
        options.circleCrop()
    }
    if (topCrop) {
        options.transform(PositionedCrop(0f, 0f))
    }
    placeHolder?.let {
        var drawable = placeHolder

        if (placeHolderRatio != null) {
            val base = placeHolderRatio.split(",")[0]
            val ratio = placeHolderRatio.split(",")[1]
            var baseW = false
            if (base.equals("W", ignoreCase = true)) {
                baseW = true
            }
            val res = ratio.trim().split(":")
            var width = 0
            var height = 0
            if (baseW) {
                val w = res[0].toInt()
                val h = res[1].toInt()
                width = if (measuredWidth == 0) screenWidth() else measuredWidth
                height = (width*1f*h/w).toInt()
                if (h <= 0) {
                    height = 200.dp2px()
                }
            }else {
                val w = res[1].toInt()
                val h = res[0].toInt()
                height = if (measuredHeight == 0) screenHeight() else measuredHeight
                width = (height*1f*w/h).toInt()
                if (w <= 0) {
                    width = 200.dp2px()
                }
            }
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, width, height)
            drawable.draw(canvas)
            drawable = BitmapDrawable(context.resources, bitmap)
        }
        if (circle || blur) {
            var thumbnail = GlideApp.with(this)
                    .load(placeHolder)
            if (circle) {
                thumbnail = thumbnail
                        .circleCrop()
            }
            if (blur) {
                thumbnail = thumbnail
                        .transform(BlurTransformation())
            }
            req = req.thumbnail(thumbnail)
        } else {
            options.placeholder(drawable)
        }
    }
    error?.let {
        var errorReq = GlideApp.with(this)
                .load(error)

        if (circle) {
            errorReq = errorReq
                    .circleCrop()
        }
        if (blur) {
            errorReq = errorReq
                    .transform(BlurTransformation())
        }
        req = req
                .error(errorReq)
    }

    fallback?.let {
        req = req.fallback(fallback)
    }
    if (blur) {
        options.transform(BlurTransformation(15, 2))
    }
    req.apply(options).into(this)

}

@BindingAdapter(value = ["liked"])
fun LikeButton.liked(liked: Boolean = false) {
    this.setLikedWithoutAnim(liked)
}

/**
 * 收藏按钮
 */
@BindingAdapter(value = ["data"])
fun LikeButton.bindAction(data: Illust) {
    action = {
        processing = true
        val star = data.is_bookmarked
        val starState: ObservableField<LoadState> = ObservableField(LoadState.Idle)
        starState.addOnPropertyChangedCallback(onPropertyChangedCallback { _, _ ->
            when(starState.get()) {
                is LoadState.Failed -> {
                    processing = false
                    if (star) {
                        like()
                    }else {
                        unlike()
                    }
                }
                is LoadState.Succeed -> {
                    processing = false
                }
            }
        })
        IllustRepository.instance
                .star(data, starState)
    }
}

@BindingConversion
fun boolToVisibility(visibility: Boolean) = if (visibility) View.VISIBLE else View.GONE

@BindingAdapter(value = ["visible"])
fun View.visible(show: Boolean = true) {
    this.visibility = if (show) View.VISIBLE else View.GONE
}

@BindingAdapter(value = ["show"])
fun View.show(show: Boolean = true) {
    this.visibility = if (show) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter(value = ["enable"])
fun View.enable(enable: Boolean = true) {
    this.isEnabled = enable
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
