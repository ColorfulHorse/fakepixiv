package com.lyj.fakepivix.app.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * @author greensun
 *
 * @date 2019/3/21
 *
 * @desc
 */
abstract class BaseBindingAdapter<T, K : BaseBindingAdapter.BaseBindingViewHolder<out ViewDataBinding>>(@LayoutRes layoutId: Int, data: MutableList<T>?) : BaseQuickAdapter<T, K>(layoutId, data) {

    constructor(@LayoutRes layoutId: Int): this(layoutId, null)

    open class BaseBindingViewHolder<B : ViewDataBinding>(view: View) : BaseViewHolder(view) {
        val binding: B? = DataBindingUtil.bind(view)
    }
}