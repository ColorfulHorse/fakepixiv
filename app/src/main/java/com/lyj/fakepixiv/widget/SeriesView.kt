package com.lyj.fakepixiv.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.lyj.fakepixiv.R

/**
 * @author greensun
 *
 * @date 2019/3/23
 *
 * @desc
 */
class SeriesView : RelativeLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.view_series, this)
    }
}