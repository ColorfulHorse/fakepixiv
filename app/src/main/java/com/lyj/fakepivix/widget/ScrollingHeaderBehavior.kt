package com.lyj.fakepivix.widget

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
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
class ScrollingHeaderBehavior : AppBarLayout.Behavior {

    constructor(): super()

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)


    override fun onStartNestedScroll(parent: CoordinatorLayout, child: AppBarLayout, directTargetChild: View, target: View, nestedScrollAxes: Int, type: Int): Boolean {
        val b = super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes, type)
        return true
    }
}