package com.lyj.fakepivix.app.adapter;

import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView;
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.util.ViewPreloadSizeProvider

import com.lyj.fakepivix.GlideApp;
import com.lyj.fakepivix.app.base.ViewModelProvider.data

/**
 * @author greensun
 * @date 2019/8/2
 * @desc
 */
abstract class PreloadBindingAdapter<T, VM : ViewDataBinding>(@LayoutRes layoutId: Int, data: List<T>, itemBindId: Int) :
        BaseBindingAdapter<T, VM>(layoutId, data, itemBindId), ListPreloader.PreloadModelProvider<T> {

    protected val sizeProvider = ViewPreloadSizeProvider<T>()
    // 最大预加载数量
    open protected var maxPreLoad = 10

    override fun bindToRecyclerView(recyclerView:RecyclerView) {
        super.bindToRecyclerView(recyclerView)
        val preloader = RecyclerViewPreloader<T>(GlideApp.with(recyclerView),   this, sizeProvider, maxPreLoad)
        recyclerView.addOnScrollListener(preloader)
    }

    override fun getPreloadItems(position: Int): MutableList<T> {
        if (data.isNotEmpty()) {
            var end = position + 1
            if (end >= data.size) {
                end = data.size - 1
            }
            if (end > position) {
                return data.subList(position, end)
            }
        }
        return mutableListOf()
    }
}
