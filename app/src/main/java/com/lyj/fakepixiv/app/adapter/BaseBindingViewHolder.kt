package com.lyj.fakepixiv.app.adapter

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.view.View
import com.chad.library.adapter.base.BaseViewHolder

open class BaseBindingViewHolder<VB : ViewDataBinding>(val view: View) : BaseViewHolder(view) {

    val binding: VB? = try {
        DataBindingUtil.bind(view)
    } catch (e: IllegalArgumentException) {
        null
    }


    constructor(binding: VB) : this(binding.root)
}