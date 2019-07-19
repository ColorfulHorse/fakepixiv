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
 * @desc 嵌套滑动机制：NestedScrollingParent2的子控件触发嵌套滑动，父控件选择消费事件（或者消费一部分），然后将剩余的交给其他继承了NestedScrollingChild2的子控件，
 * 子控件选择消费得到的事件（或者消费一部分），然后再将最后剩余的事件回传给父控件
 *
 * 这里拓展了CoordinatorLayout使它拥有NestedScrollingChild2功能，可以将自己的滑动事件交给父控件
 */
class SubCoordinatorLayout : CoordinatorLayout, NestedScrollingChild2 {
    var reverse = false

    val mChildHelper = NestedScrollingChildHelper(this).apply {
        isNestedScrollingEnabled = true
    }
    val windowOffset = IntArray(2)

    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, @AttrRes defStyleAttr: Int): super(context, attributeSet, defStyleAttr)

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
        if (dy > 0) {
            if (reverse) {
                dispatchSelf(target, dx, dy, consumed, type)
            }else {
                // 上滑时先让父控件消费
                dispatchParent(dy, dx, consumed, type, target)
            }
        }else {
            if (reverse) {
                dispatchParent(dy, dx, consumed, type, target)
            }else {
                // 下滑时自己先消费
                dispatchSelf(target, dx, dy, consumed, type)
            }
        }
    }

    /**
     * 先自己消费
     */
    private fun dispatchSelf(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        super.onNestedPreScroll(target, dx, dy, consumed, type)
        val unConsumed = dy - consumed[1]
        if (unConsumed != 0) {
            if (dispatchNestedPreScroll(dx, unConsumed, consumed, windowOffset, type)) {
                dispatchNestedScroll(0, consumed[1], 0, unConsumed, windowOffset, 0)
            }
        }
    }

    /**
     * 先让父控件消费
     */
    private fun dispatchParent(dy: Int, dx: Int, consumed: IntArray, type: Int, target: View) {
        var delta = dy
        if (dispatchNestedPreScroll(dx, dy, consumed, windowOffset, type)) {
            val unConsumed = dy - consumed[1]
            dispatchNestedScroll(0, consumed[1], 0, unConsumed, windowOffset, 0)
            delta = unConsumed
        }
        if (delta != 0) {
            super.onNestedPreScroll(target, dx, delta, consumed, type)
        }
    }

    override fun onNestedFling(target: View, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        return true
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        return this.dispatchNestedPreFling(velocityX, velocityY)
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        stopNestedScroll(type)
        super.onStopNestedScroll(target, type)
    }

}