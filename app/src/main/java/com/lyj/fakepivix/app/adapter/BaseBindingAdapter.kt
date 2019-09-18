package com.lyj.fakepivix.app.adapter

import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.lyj.fakepivix.BR

/**
 * @author greensun
 *
 * @date 2019/3/21
 *
 * @desc
 */
open class BaseBindingAdapter<T, VB : ViewDataBinding>(@LayoutRes layoutId: Int, data: MutableList<T>, val itemBindId: Int) : BaseQuickAdapter<T, BaseBindingViewHolder<VB>>(layoutId, data) {

    init {
        if(data is ObservableList<T>) {
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

    override fun convert(helper: BaseBindingViewHolder<VB>, item: T) {
        helper.binding?.setVariable(itemBindId, item)
        helper.binding?.setVariable(BR.position, helper.adapterPosition - headerLayoutCount)
    }

    override fun createBaseViewHolder(view: View): BaseBindingViewHolder<VB> {
        return BaseBindingViewHolder(view)
    }




    fun getRecyView(): RecyclerView {
        return recyclerView
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