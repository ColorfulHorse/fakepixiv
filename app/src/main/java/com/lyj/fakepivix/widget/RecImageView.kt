package com.lyj.fakepivix.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.lyj.fakepivix.app.App.Companion.context

/**
 * @author greensun
 *
 * @date 2019/3/23
 *
 * @desc
 */
class RecImageView : ImageView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val mode = MeasureSpec.getMode(heightMeasureSpec)
        val h = MeasureSpec.makeMeasureSpec(width, mode)
        setMeasuredDimension(width, h)
    }
}