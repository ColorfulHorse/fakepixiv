package com.lyj.fakepixiv.app.databinding

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewAnimator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnCancel
import androidx.core.view.updateLayoutParams
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.flyco.tablayout.listener.OnTabSelectListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.lyj.fakepixiv.GlideApp
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.data.source.remote.IllustRepository
import com.lyj.fakepixiv.app.glide.PositionedCrop
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.app.utils.PaddingAnimationFactory
import com.lyj.fakepixiv.app.utils.dp2px
import com.lyj.fakepixiv.app.utils.screenHeight
import com.lyj.fakepixiv.app.utils.screenWidth
import com.lyj.fakepixiv.widget.CommonTabLayout
import com.lyj.fakepixiv.widget.LikeButton
import jp.wasabeef.glide.transformations.BlurTransformation
import me.yokeyword.fragmentation.SupportHelper


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
        req = if (placeHolderRatio != null) {
            req.transition(DrawableTransitionOptions.with(PaddingAnimationFactory<Drawable>(factory)))
        } else {
            req.transition(DrawableTransitionOptions.withCrossFade(factory))
        }
    }
    val options = RequestOptions()
    if (circle) {
        options.circleCrop()
    }
    if (topCrop) {
        options.transform(PositionedCrop(0.5f, 0f))
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
                height = (width * 1f * h / w).toInt()
                if (h <= 0) {
                    height = 200.dp2px()
                }
            } else {
                val w = res[1].toInt()
                val h = res[0].toInt()
                height = if (measuredHeight == 0) screenHeight() else measuredHeight
                width = (height * 1f * w / h).toInt()
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



@BindingAdapter(value = ["visible"])
fun View.visible(show: Boolean?) {
    show?.let {
        visibility = if (it) View.VISIBLE else View.GONE
    }
}

@BindingAdapter(value = ["show"])
fun View.show(show: Boolean?) {
    show?.let {
        visibility = if (it) View.VISIBLE else View.INVISIBLE
    }
}

@BindingAdapter(value = ["enable"])
fun View.enable(enable: Boolean = true) {
    this.isEnabled = enable
}

@BindingAdapter(value = ["height", "width"], requireAll = false)
fun View.size(height: Number? = null, width: Number? = null) {
    updateLayoutParams<ViewGroup.LayoutParams> {
        width?.let {
            this.width = it.toInt()
        }
        height?.let {
            this.height = it.toInt()
        }
    }
}

@BindingAdapter(value = ["html"])
fun TextView.html(content: String) {
    this.text = Html.fromHtml(content)
}


@BindingAdapter(value = ["show"])
fun FloatingActionButton.show(show: Boolean = true) {
    if (show) this.show() else this.hide()
}


@BindingAdapter(value = ["fadeIn", "duration"], requireAll = false)
fun View.fadeIn(show: Boolean?, duration: Long?) {
    show?.let {
        if (isShown == show) {
            return
        }
        val d = duration ?: 200L
        val anim = this.animate().alpha(if (show) 1f else 0f)
        anim.setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                if (!show) {
                    visibility = View.GONE
                }
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {
                if (show) {
                    visibility = View.VISIBLE
                }
            }

        })
        anim.duration = d
        anim.start()
    }
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
    } else {
        lp.dimensionRatio = dimensionRatio
    }
    this.layoutParams = lp
}

@BindingAdapter(value = ["keyboardShow"])
fun EditText.config(keyboardShow: Boolean) {
    if (keyboardShow) {
        SupportHelper.showSoftInput(this)
    } else {
        SupportHelper.hideSoftInput(this)
    }
}


@BindingAdapter(value = ["keyboardListener"])
fun View.keyboardListener(listener: KeyboardListener) {
    val root = (context as Activity).window.decorView
    var rootViewHeight = -1

    val callback = object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            val rect = Rect()
            root.getWindowVisibleDisplayFrame(rect)
            if (rootViewHeight == -1) {
                rootViewHeight = rect.height()
                return
            }

            // 没有改变
            if (rootViewHeight == rect.height()) {
                return
            }

            if (rootViewHeight - rect.height() > 200) {
                if (getLocalVisibleRect(Rect())) {
                    listener.onKeyboardChanged(true)
                }
                rootViewHeight = rect.height()
                return
            }

            if (rect.height() - rootViewHeight > 200) {
                if (getLocalVisibleRect(Rect())) {
                    listener.onKeyboardChanged(false)
                }
                rootViewHeight = rect.height()
            }
        }
    }
    root.viewTreeObserver.addOnGlobalLayoutListener(callback)
    addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        override fun onViewDetachedFromWindow(v: View?) {
            root.viewTreeObserver.removeOnGlobalLayoutListener(callback)
        }

        override fun onViewAttachedToWindow(v: View?) {
        }

    })
}

// 键盘监听
interface KeyboardListener {
    fun onKeyboardChanged(isOpen: Boolean)
}


//@BindingAdapter(value = ["onTabSelect", "onTabReSelect"], requireAll = false)
//fun CommonTabLayout.onTabSelect(onTabSelect: (Int) -> Unit, onTabReSelect: (Int) -> Unit) {
//    this.setOnTabSelectListener(object : OnTabSelectListener {
//        override fun onTabSelect(position: Int) {
//            onTabSelect(position)
//        }
//
//        override fun onTabReselect(position: Int) {
//            onTabReSelect(position)
//        }
//
//    })
//}

@BindingAdapter(value = ["onTabSelect", "onTabReSelect"], requireAll = false)
fun CommonTabLayout.onTabListener(onTabSelect: TabSelectListener? = null, onTabReSelect: TabSelectListener? = null) {
    this.setOnTabSelectListener(object : OnTabSelectListener {
        override fun onTabSelect(position: Int) {
            onTabSelect?.onTabSelect(position)
        }

        override fun onTabReselect(position: Int) {
            onTabReSelect?.onTabSelect(position)
        }

    })
}

@BindingAdapter(value = ["errorText"], requireAll = false)
fun TextInputLayout.config(errorText: String? = null) {
    error = errorText
}

interface TabSelectListener {
    fun onTabSelect(position: Int)
}


@BindingConversion
fun boolToVisibility(visibility: Boolean) = if (visibility) View.VISIBLE else View.GONE


@BindingConversion
fun stateToVisibility(loadState: LoadState) = if (loadState is LoadState.Loading) View.VISIBLE else View.GONE
