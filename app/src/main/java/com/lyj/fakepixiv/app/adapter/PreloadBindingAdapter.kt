package com.lyj.fakepixiv.app.adapter;

import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView;
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.util.ViewPreloadSizeProvider

import com.lyj.fakepixiv.GlideApp;

/**
 * @author greensun
 * @date 2019/8/2
 * @desc
 */
abstract class PreloadBindingAdapter<T : PreloadModel, VM : ViewDataBinding>(@LayoutRes layoutId: Int, data: MutableList<T>, itemBindId: Int) :
        BaseBindingAdapter<T, VM>(layoutId, data, itemBindId), ListPreloader.PreloadModelProvider<String> {

    open protected var sizeProvider: ListPreloader.PreloadSizeProvider<String> = ViewPreloadSizeProvider<String>()
    // 最大预加载数量
    open protected var maxPreLoad = 10

    override fun bindToRecyclerView(recyclerView: RecyclerView) {
        super.bindToRecyclerView(recyclerView)
        val preloader = RecyclerViewPreloader<String>(GlideApp.with(recyclerView), this, sizeProvider, maxPreLoad)
        recyclerView.addOnScrollListener(preloader)
    }

    /**
     * 解决插入特定view引起问题
     */
    open fun getRealPosition(position: Int): Int {
        return position
    }

    override fun getPreloadItems(position: Int): List<String> {
        if (data.isNotEmpty()) {
            val realPos = getRealPosition(position);
            if (realPos < data.size - 1) {
                return data[realPos + 1].getPreloadUrls()
            }
        }
        return listOf()
    }

    override fun getPreloadRequestBuilder(item: String): RequestBuilder<*>? {
        return GlideApp.with(mContext)
                .load(item)
    }
}
