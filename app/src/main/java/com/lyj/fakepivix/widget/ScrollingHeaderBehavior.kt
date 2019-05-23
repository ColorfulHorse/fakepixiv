package com.lyj.fakepivix.widget

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import com.lyj.fakepivix.app.App.Companion.context

/**
 * @author greensun
 *
 * @date 2019/5/21
 *
 * @desc
 */
class ScrollingHeaderBehavior : CoordinatorLayout.Behavior<View> {

    constructor(): super()

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)


    override fun onStartNestedScroll(parent: CoordinatorLayout, child: View, directTargetChild: View, target: View, nestedScrollAxes: Int, type: Int): Boolean {
        val b = super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes, type)
        parent.translationX
        parent.scrollY
        return true
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        //super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        //ViewCompat.offsetTopAndBottom(coordinatorLayout, -dy)
        val hidden = (dy > 0) and (coordinatorLayout.scrollY < child.height)
        val show = (dy < 0) and (coordinatorLayout.scrollY > 0) and (!target.canScrollVertically(-1))
        if (hidden or show) {
            coordinatorLayout.scrollBy(0,  dy)
            consumed[1] = dy
        }
    }

    override fun onNestedPreFling(coordinatorLayout: CoordinatorLayout, child: View, target: View, velocityX: Float, velocityY: Float): Boolean {
        val hidden = (velocityY > 0) and (coordinatorLayout.scrollY < child.height)
        val show = (velocityY < 0) and (coordinatorLayout.scrollY > 0) and (target.canScrollVertically(-1))
        if (hidden or show) {
            return true
        }
        return false
    }

    override fun onNestedFling(coordinatorLayout: CoordinatorLayout, child: View, target: View, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed)
    }

}