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
        return true
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        //super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        var result = dy
        if (dy > 0) {
            if (coordinatorLayout.scrollY == child.height)
                return
            if (coordinatorLayout.scrollY + dy > child.height) {
                result = child.height - coordinatorLayout.scrollY
            }
            coordinatorLayout.scrollBy(0,  result)
            consumed[1] = result
        }else {
            if (coordinatorLayout.scrollY == 0)
                return
            if (coordinatorLayout.scrollY + dy < 0) {
                result = - coordinatorLayout.scrollY
            }
            coordinatorLayout.scrollBy(0,  result)
            consumed[1] = result
        }
//        val hidden = (dy > 0) and (coordinatorLayout.scrollY < child.height)
//        val show = (dy < 0) and (coordinatorLayout.scrollY > 0) and (target.canScrollVertically(-1))
//        if (hidden or show) {
//            coordinatorLayout.scrollBy(0,  dy)
//            consumed[1] = dy
//        }
//        var result = - dy
//        var target = child.top - dy
//        if (dy > 0) {
//            if (target == - child.height) {
//                return
//            }
//            if (target < - child.height) {
//                result = - child.height - child.top
//            }
//        }else {
//            if (target == 0) {
//                return
//            }
//            if (target > 0) {
//                result = - child.top
//            }
//        }
//        ViewCompat.offsetTopAndBottom(child, result)
//        consumed[1] = dy
    }

    override fun onNestedPreFling(coordinatorLayout: CoordinatorLayout, child: View, target: View, velocityX: Float, velocityY: Float): Boolean {
//        val hidden = (velocityY > 0) and (coordinatorLayout.scrollY < child.height)
//        val show = (velocityY < 0) and (coordinatorLayout.scrollY > 0) and (target.canScrollVertically(-1))
//        if (hidden or show) {
//            return true
//        }
        return false
    }

    override fun onNestedFling(coordinatorLayout: CoordinatorLayout, child: View, target: View, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed)
    }

}