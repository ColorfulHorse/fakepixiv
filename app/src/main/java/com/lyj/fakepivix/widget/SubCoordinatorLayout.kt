package com.lyj.fakepivix.widget

import android.content.Context
import android.support.annotation.AttrRes
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.NestedScrollingChild2
import android.support.v4.view.NestedScrollingChildHelper
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View

/**
 * @author greensun
 *
 * @date 2019/5/23
 *
 * @desc
 */
class SubCoordinatorLayout : CoordinatorLayout, NestedScrollingChild2 {
    val mChildHelper = NestedScrollingChildHelper(this).apply {
        isNestedScrollingEnabled = true
    }
    var lastX = 0
    var lastY = 0
    var mNestedYOffset = 0
    val comsumed = IntArray(2)
    val windowOffset = IntArray(2)

    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, @AttrRes defStyleAttr: Int): super(context, attributeSet, defStyleAttr)

//    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
//        when(ev.action) {
//            MotionEvent.ACTION_DOWN -> {
//                lastX = ev.x.toInt()
//                lastY = ev.y.toInt()
//                var axes = ViewCompat.SCROLL_AXIS_NONE
//                if (canScrollHorizontally(-1)) {
//                    axes = ViewCompat.SCROLL_AXIS_HORIZONTAL
//                }
//                if (canScrollVertically(-1)) {
//                    axes = ViewCompat.SCROLL_AXIS_VERTICAL
//                }
//                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_TOUCH)
//            }
//            MotionEvent.ACTION_MOVE -> {
//                dispatchNestedPreScroll(lastX - ev.x.toInt(), lastY - ev.y.toInt(), comsumed, windowOffset, ViewCompat.TYPE_TOUCH)
//                lastX = ev.x.toInt()
//                lastY = ev.y.toInt()
//            }
//        }
//        return super.onTouchEvent(ev)
//    }

//    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//        var b = false
//        when(ev.action) {
//            MotionEvent.ACTION_DOWN -> {
//                lastX = ev.x.toInt()
//                lastY = ev.y.toInt()
//                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_TOUCH)
//            }
//            MotionEvent.ACTION_MOVE -> {
//                var deltaY = lastY - ev.y.toInt()
//                dispatchNestedPreScroll(0, deltaY, comsumed, windowOffset, 0)
//                val unconsumedY = deltaY - comsumed[1]
//                dispatchNestedScroll(0, comsumed[1], 0, unconsumedY, windowOffset, 0)
//                //stopNestedScroll(ViewCompat.TYPE_TOUCH)
//                lastX = ev.x.toInt()
//                lastY = ev.y.toInt()
//            }
//            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
//                stopNestedScroll(ViewCompat.TYPE_TOUCH)
//            }
//        }
//        return super.dispatchTouchEvent(ev)
//    }

    override fun setNestedScrollingEnabled(enabled: Boolean) {
        mChildHelper.isNestedScrollingEnabled = enabled
    }

    override fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, offsetInWindow: IntArray?, type: Int): Boolean {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type)
    }

    override fun startNestedScroll(axes: Int, type: Int): Boolean {
        return mChildHelper.startNestedScroll(axes, type)
    }

    override fun dispatchNestedPreScroll(dx: Int, dy: Int, consumed: IntArray?, offsetInWindow: IntArray?, type: Int): Boolean {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type)
    }

    override fun stopNestedScroll(type: Int) {
        mChildHelper.stopNestedScroll(type)
    }

    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY)
    }

    override fun dispatchNestedFling(velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed)
    }

    override fun hasNestedScrollingParent(type: Int): Boolean {
       return mChildHelper.hasNestedScrollingParent(type)
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, type)
        return super.onStartNestedScroll(child, target, axes, type)
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        var delta = dy
        if (dispatchNestedPreScroll(dx, dy, consumed, windowOffset, type)) {
            val unConsumed = dy - consumed[1]
            dispatchNestedScroll(0, comsumed[1], 0, unConsumed, windowOffset, 0)
            delta = unConsumed
        }
        if (delta != 0) {
            super.onNestedPreScroll(target, dx, delta, consumed, type)
        }
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        stopNestedScroll(type)
        super.onStopNestedScroll(target, type)
    }

}