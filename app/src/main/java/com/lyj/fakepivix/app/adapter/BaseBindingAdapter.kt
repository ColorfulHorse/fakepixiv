package com.lyj.fakepivix.app.adapter

import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * @author greensun
 *
 * @date 2019/3/21
 *
 * @desc
 */
open class BaseBindingAdapter<T, VB : ViewDataBinding>(@LayoutRes layoutId: Int, val data: ObservableList<T>, val itemBindId: Int) : BaseQuickAdapter<T, BaseBindingViewHolder<VB>>(layoutId, data) {

    init {
        data.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableList<T>>(){
            override fun onChanged(sender: ObservableList<T>?) {
                notifyDataSetChanged()
            }

            override fun onItemRangeRemoved(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {

                notifyItemRangeRemoved(positionStart + headerLayoutCount, itemCount)
            }

            override fun onItemRangeMoved(sender: ObservableList<T>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
                if (itemCount == 1) {
                    notifyItemMoved(fromPosition + headerLayoutCount, toPosition + headerLayoutCount)
                }else {
                    notifyDataSetChanged()
                }
            }

            override fun onItemRangeInserted(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
                notifyItemRangeInserted(positionStart + headerLayoutCount, itemCount)
                val dataSize = if (mData == null) 0 else mData.size
                if (dataSize == itemCount) {
                    notifyDataSetChanged()
                }
            }

            override fun onItemRangeChanged(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
                notifyItemRangeChanged(positionStart + headerLayoutCount, itemCount)
            }

        })
    }

    override fun convert(helper: BaseBindingViewHolder<VB>, item: T) {
        helper.binding?.setVariable(itemBindId, item)
    }



    fun getRecyView(): RecyclerView {
        return recyclerView
    }
}