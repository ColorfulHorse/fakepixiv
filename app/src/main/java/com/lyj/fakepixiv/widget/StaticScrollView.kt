package com.lyj.fakepixiv.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.widget.NestedScrollView

/**
 * @author green sun
 *
 * @date 2020/1/2
 *
 * @desc
 */
class StaticScrollView : NestedScrollView {
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun requestChildFocus(child: View?, focused: View?) {

    }
}