package com.lyj.fakepixiv.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.WindowInsets
import android.widget.RelativeLayout
import androidx.annotation.AttrRes

/**
 * @author green sun
 *
 * @date 2019/12/11
 *
 * @desc 修复软键盘遮盖问题
 */
class FitRelativeLayout : RelativeLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, @AttrRes defStyleAttr: Int) : super(context, attributeSet, defStyleAttr)


    override fun fitSystemWindows(insets: Rect): Boolean {
        insets.left = 0
        insets.top = 0
        insets.right = 0
        return super.fitSystemWindows(insets)
    }

    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
        return super.onApplyWindowInsets(insets.replaceSystemWindowInsets(0, 0, 0, insets.systemWindowInsetBottom))
    }

}