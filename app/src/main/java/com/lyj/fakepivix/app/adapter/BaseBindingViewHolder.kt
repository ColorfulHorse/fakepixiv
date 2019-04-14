package com.lyj.fakepivix.app.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.View
import com.chad.library.adapter.base.BaseViewHolder

open class BaseBindingViewHolder<VB : ViewDataBinding>(view: View) : BaseViewHolder(view) {
    val binding: VB? = DataBindingUtil.bind(view)
}