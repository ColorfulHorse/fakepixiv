package com.lyj.fakepivix.app.adapter

import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.util.SparseIntArray
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * @author greensun
 *
 * @date 2019/4/13
 *
 * @desc
 */
class BaseMultiBindingAdapter<T : MultiItemEntity>(data: ObservableList<T>) : BaseMultiItemQuickAdapter<T, BaseBindingViewHolder<ViewDataBinding>>(data) {

    private val variableIds = SparseIntArray()

    /**
     * [variableId] layout中变量对应的BR.xxx id
     */
    fun addItemType(type: Int, @LayoutRes layoutResId: Int, variableId: Int) {
        variableIds.put(type, variableId)
    }


    override fun convert(helper: BaseBindingViewHolder<ViewDataBinding>, item: T) {
        val id = variableIds[item.itemType, -1]
        if (id != -1) {
            helper.binding?.setVariable(id, item)
        }
    }

}