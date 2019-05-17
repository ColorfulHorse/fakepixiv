package com.lyj.fakepivix.app.adapter

import android.databinding.ObservableList
import android.support.v7.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.cache.DiskCache
import com.bumptech.glide.util.ViewPreloadSizeProvider
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.lyj.fakepivix.GlideApp

/**
 * @author greensun
 *
 * @date 2019/4/29
 *
 * @desc 预加载adapter
 */
abstract class PreloadMultiBindingAdapter<T : MultiItemEntity>(data: ObservableList<T>) : BaseMultiBindingAdapter<T>(data), ListPreloader.PreloadModelProvider<T> {
    protected val sizeProvider = ViewPreloadSizeProvider<T>()
    // 最大预加载数量
    open protected var maxPreLoad = 10

    override fun bindToRecyclerView(recyclerView: RecyclerView) {
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