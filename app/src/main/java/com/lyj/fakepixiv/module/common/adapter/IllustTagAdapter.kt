package com.lyj.fakepixiv.module.common.adapter

import androidx.databinding.ViewDataBinding
import androidx.annotation.LayoutRes
import androidx.databinding.library.baseAdapters.BR
import com.lyj.fakepixiv.app.adapter.BaseBindingAdapter
import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.data.model.response.Tag
import com.lyj.fakepixiv.app.utils.Router

/**
 * @author greensun
 *
 * @date 2019/9/11
 *
 * @desc
 */
class IllustTagAdapter(@IllustCategory category: String, @LayoutRes layoutId: Int, data: MutableList<Tag>): BaseBindingAdapter<Tag, ViewDataBinding>(layoutId, data, BR.data) {

    init {
        setOnItemClickListener { baseQuickAdapter, view, i ->
            Router.goSearch(category, mData[i].name)
        }
    }
}