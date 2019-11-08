package com.lyj.fakepixiv.app.adapter

import androidx.databinding.ViewDataBinding
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.util.ViewPreloadSizeProvider

import com.lyj.fakepixiv.GlideApp;
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.databinding.ItemWallpaperBinding

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

    open @IdRes val preloadId = R.id.image

    override fun bindToRecyclerView(recyclerView: RecyclerView) {
        super.bindToRecyclerView(recyclerView)
        val preloader = RecyclerViewPreloader<String>(GlideApp.with(recyclerView), this, sizeProvider, maxPreLoad)
        recyclerView.addOnScrollListener(preloader)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<VM> {
        val vh = super.onCreateViewHolder(parent, viewType)
        if (sizeProvider is ViewPreloadSizeProvider) {
            val iv = vh.getView<ImageView>(preloadId)
            iv?.let {
                (sizeProvider as ViewPreloadSizeProvider).setView(iv)
            }
        }
        return vh
    }

    /**
     * 解决插入特定view引起问题
     */
    open fun getRealPosition(position: Int): Int {
        return position
    }

    override fun getPreloadItems(position: Int): List<String> {
        if (data.isNotEmpty()) {
            val realPos = getRealPosition(position)
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
