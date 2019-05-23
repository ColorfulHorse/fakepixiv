package com.lyj.fakepivix.widget

import android.content.Context
import android.support.annotation.AttrRes
import android.support.coordinatorlayout.R
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.NestedScrollingChild2
import android.util.AttributeSet

/**
 * @author greensun
 *
 * @date 2019/5/23
 *
 * @desc
 */
class SubCoordinatorLayout : CoordinatorLayout, NestedScrollingChild2 {

    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, @AttrRes defStyleAttr: Int): super(context, attributeSet, defStyleAttr)

    override fun dispatchNestedScroll(p0: Int, p1: Int, p2: Int, p3: Int, p4: IntArray?, p5: Int): Boolean {
        return true
    }

    override fun startNestedScroll(p0: Int, p1: Int): Boolean {
        return true
    }

    override fun dispatchNestedPreScroll(p0: Int, p1: Int, p2: IntArray?, p3: IntArray?, p4: Int): Boolean {
        return true
    }

    override fun stopNestedScroll(p0: Int) {

    }

    override fun hasNestedScrollingParent(p0: Int): Boolean {
       return true
    }

}