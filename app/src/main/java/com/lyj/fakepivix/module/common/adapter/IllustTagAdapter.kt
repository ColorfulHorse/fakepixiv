package com.lyj.fakepivix.module.common.adapter

import android.databinding.ViewDataBinding
import androidx.annotation.LayoutRes
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.app.adapter.BaseBindingAdapter
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.data.model.response.Tag
import com.lyj.fakepivix.app.utils.Router

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