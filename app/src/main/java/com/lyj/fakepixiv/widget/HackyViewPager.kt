package com.lyj.fakepixiv.widget

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager
import android.view.MotionEvent



/**
 * @author green sun
 *
 * @date 2019/12/20
 *
 * @desc
 */
class HackyViewPager : ViewPager {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return try {
            super.onInterceptTouchEvent(ev)
        } catch (e: IllegalArgumentException) {
            false
        }

    }
}