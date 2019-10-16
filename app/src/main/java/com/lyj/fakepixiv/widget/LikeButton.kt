package com.lyj.fakepixiv.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.view.animation.RotateAnimation
import com.lyj.fakepixiv.R


/**
 * @author greensun
 *
 * @date 2019/4/23
 *
 * @desc
 */
class LikeButton : RelativeLayout {
    private var normal: ImageView
    private var selected: ImageView
    private var effect: View
    var liked = false
    var processing = false
    var action: (() -> Unit)? = null

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_likebutton, this, true)
        normal = findViewById(R.id.unselected)
        selected = findViewById(R.id.selected)
        effect = findViewById(R.id.effect)

        setOnClickListener {
            if (!processing) {
                if (liked) {
                    unlike()
                }else {
                    like()
                }
                liked = !liked
                action?.invoke()
            }
        }
    }


     fun setLikedWithoutAnim(liked: Boolean) {
        if(liked) {
            normal.visibility = View.INVISIBLE
            selected.visibility = View.VISIBLE
        }else {
            normal.visibility = View.VISIBLE
            selected.visibility = View.INVISIBLE
        }
        this.liked = liked
    }

    fun like() {
        val minify = ScaleAnimation(1.0f, 0.1f, 1.0f, 0.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
            duration = 100
        }
        val magnify = ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
            duration = 200
            interpolator = OvershootInterpolator()
        }
        val effectScale = ScaleAnimation(1f, 1.2f, 1f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        val effectAlpha = AlphaAnimation(1f, 0f)
        val set = AnimationSet(false)
        set.addAnimation(effectScale)
        set.addAnimation(effectAlpha)
        set.interpolator = DecelerateInterpolator()
        set.duration = 200
        set.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                effect.visibility = View.INVISIBLE
            }

            override fun onAnimationStart(animation: Animation?) {

            }

        })
        minify.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                normal.visibility = View.INVISIBLE
                selected.visibility = View.VISIBLE
                selected.startAnimation(magnify)
                effect.startAnimation(set)
            }

            override fun onAnimationStart(animation: Animation?) {

            }

        })
        normal.startAnimation(minify)

    }

    fun unlike() {
        val magnify = ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
            duration = 500
        }
        val alphaAnim = AlphaAnimation(1f, 0f).apply { duration = 300 }
        val translateAnim = TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1.5f)
                .apply {
                    duration = 500
                }
        val rotateAnim = RotateAnimation(0.0f, 18.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
                .apply {
                    duration = 500
                }
        val set = AnimationSet(true)
        set.addAnimation(alphaAnim)
        set.addAnimation(translateAnim)
        set.addAnimation(rotateAnim)
        set.duration = 500
        set.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                selected.visibility = View.INVISIBLE
            }

            override fun onAnimationStart(animation: Animation?) {

            }

        })
        normal.visibility = View.VISIBLE
        normal.startAnimation(magnify)
        selected.startAnimation(set)
    }
}