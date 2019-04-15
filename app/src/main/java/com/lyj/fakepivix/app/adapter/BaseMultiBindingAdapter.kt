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
open class BaseMultiBindingAdapter<T : MultiItemEntity>(data: ObservableList<T>) : BaseMultiItemQuickAdapter<T, BaseBindingViewHolder<ViewDataBinding>>(data) {

    private val variableIds = SparseIntArray()

    init {
        data.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableList<T>>(){
            override fun onChanged(sender: ObservableList<T>?) {
                notifyDataSetChanged()
            }

            override fun onItemRangeRemoved(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
                notifyItemRangeRemoved(positionStart, itemCount)
            }

            override fun onItemRangeMoved(sender: ObservableList<T>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
                if (itemCount == 1) {
                    notifyItemMoved(fromPosition, toPosition)
                }else {
                    notifyDataSetChanged()
                }
            }

            override fun onItemRangeInserted(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
                notifyItemRangeInserted(positionStart, itemCount)
            }

            override fun onItemRangeChanged(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
                notifyItemRangeChanged(positionStart, itemCount)
            }

        })
    }

    /**
     * [variableId] layout中变量对应的BR.xxx id
     */
    fun addItemType(type: Int, @LayoutRes layoutResId: Int, variableId: Int) {
        addItemType(type, layoutResId)
        variableIds.put(type, variableId)
    }


    override fun convert(helper: BaseBindingViewHolder<ViewDataBinding>, item: T) {
        val id = variableIds[item.itemType, -1]
        if (id != -1) {
            helper.binding?.setVariable(id, item)
        }
    }

}