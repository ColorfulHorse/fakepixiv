package com.lyj.fakepixiv.app.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.View
import com.chad.library.adapter.base.BaseViewHolder

open class BaseBindingViewHolder<VB : ViewDataBinding>(view: View) : BaseViewHolder(view) {

    constructor(binding: ViewDataBinding): this(binding.root)

    val binding: VB? = let {
        try {
            DataBindingUtil.bind(view)
        }catch (e: IllegalArgumentException) {
            null
        }
    }

}