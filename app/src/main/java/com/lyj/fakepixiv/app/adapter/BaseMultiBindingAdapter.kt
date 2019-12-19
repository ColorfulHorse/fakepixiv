package com.lyj.fakepixiv.app.adapter

import androidx.databinding.ObservableList
import androidx.databinding.ViewDataBinding
import androidx.annotation.LayoutRes
import android.util.SparseIntArray
import android.view.View
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import androidx.databinding.library.baseAdapters.BR

/**
 * @author greensun
 *
 * @date 2019/4/13
 *
 * @desc
 */
open class BaseMultiBindingAdapter<T : MultiItemEntity>(data: MutableList<T>) : BaseMultiItemQuickAdapter<T, BaseBindingViewHolder<ViewDataBinding>>(data) {

    private val variableIds = SparseIntArray()

    init {
        if (data is ObservableList<T>) {
            data.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableList<T>>(){
                override fun onChanged(sender: ObservableList<T>?) {
                    notifyDataSetChanged()
                }

                override fun onItemRangeRemoved(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
                    notifyItemRangeRemoved(positionStart, itemCount)
                    compatibilityDataSizeChanged(0)
                }

                override fun onItemRangeMoved(sender: ObservableList<T>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
                    if (itemCount == 1) {
                        notifyItemMoved(fromPosition, toPosition)
                    }else {
                        notifyDataSetChanged()
                    }
                }

                override fun onItemRangeInserted(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
                    var start = positionStart
                    if (positionStart > 0) {
                        val count = getItemCount() - data.size
                        start += count
                    }
                    notifyItemRangeInserted(start, itemCount)
                    if (start == 0) {
                        recyclerView?.scrollToPosition(0)
                    }
                    compatibilityDataSizeChanged(itemCount)
                }

                override fun onItemRangeChanged(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
                    notifyItemRangeChanged(positionStart, itemCount)
                }

            })
        }
    }

    /**
     * [variableId] layout中变量对应的BR.xxx id
     */
    fun addItemType(type: Int, @LayoutRes layoutResId: Int, variableId: Int?) {
        addItemType(type, layoutResId)
        variableId?.let {
            variableIds.put(type, variableId)
        }
    }


    override fun convert(helper: BaseBindingViewHolder<ViewDataBinding>, item: T) {
        val id = variableIds[item.itemType, -1]
        if (id != -1) {
            helper.binding?.setVariable(id, item)
        }
        helper.binding?.setVariable(BR.position, helper.adapterPosition - headerLayoutCount)
    }

    override fun createBaseViewHolder(view: View): BaseBindingViewHolder<ViewDataBinding> {
        return BaseBindingViewHolder(view)
    }

    /**
     * compatible getLoadMoreViewCount and getEmptyViewCount may change
     *
     * @param size Need compatible data size
     */
    private fun compatibilityDataSizeChanged(size: Int) {
        val dataSize = if (mData == null) 0 else mData.size
        if (dataSize == size) {
            notifyDataSetChanged()
        }
    }

}