package com.lyj.fakepixiv.widget

import android.content.Context
import androidx.coordinatorlayout.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View

/**
 * @author greensun
 *
 * @date 2019/5/21
 *
 * @desc
 */
open class SubBehavior : CoordinatorLayout.Behavior<View> {
    private lateinit var dependency: View

    constructor(): super()

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        val wm = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val hm = View.MeasureSpec.makeMeasureSpec(parent.height - dependency.bottom, View.MeasureSpec.EXACTLY)
        child.measure(wm, hm)
        child.layout(parent.left, dependency.bottom, parent.right, parent.bottom)
        return true
    }

    override fun onMeasureChild(parent: CoordinatorLayout, child: View, parentWidthMeasureSpec: Int, widthUsed: Int, parentHeightMeasureSpec: Int, heightUsed: Int): Boolean {
        val lp = child.layoutParams as CoordinatorLayout.LayoutParams
        dependency = parent.findViewById<View>(lp.anchorId)
        val pw = View.MeasureSpec.getSize(parentWidthMeasureSpec)
        val ph = View.MeasureSpec.getSize(parentHeightMeasureSpec)
        val wm = View.MeasureSpec.makeMeasureSpec(pw, View.MeasureSpec.EXACTLY)
        val hm = View.MeasureSpec.makeMeasureSpec(ph - dependency.bottom, View.MeasureSpec.EXACTLY)
        child.measure(wm, hm)
        return true
    }

    override fun onLayoutChild(parent: CoordinatorLayout, child: View, layoutDirection: Int): Boolean {
        child.layout(parent.left, dependency.bottom, parent.right, parent.bottom)
        return true
    }
}