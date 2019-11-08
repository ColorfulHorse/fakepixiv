package com.lyj.fakepixiv.app.adapter

import androidx.databinding.ViewDataBinding
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.util.ViewPreloadSizeProvider
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.lyj.fakepixiv.GlideApp
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.data.model.bean.MultiPreloadItem

/**
 * @author greensun
 *
 * @date 2019/4/29
 *
 * @desc 预加载adapter
 */
abstract class PreloadMultiBindingAdapter<T : MultiPreloadItem>(data: MutableList<T>) : BaseMultiBindingAdapter<T>(data), ListPreloader.PreloadModelProvider<String> {
    protected val sizeProvider: ListPreloader.PreloadSizeProvider<String> = ViewPreloadSizeProvider<String>()
    // 最大预加载数量
    open protected var maxPreLoad = 10

    open @IdRes val preloadId = R.id.image

    // 是否开启预加载
    open var usePreload = true

    override fun bindToRecyclerView(recyclerView: RecyclerView) {
        super.bindToRecyclerView(recyclerView)
        val preloader = RecyclerViewPreloader<String>(GlideApp.with(recyclerView), this, sizeProvider, maxPreLoad)
        if (usePreload) {
            recyclerView.addOnScrollListener(preloader)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<ViewDataBinding> {
        val vh = super.onCreateViewHolder(parent, viewType)
        if (sizeProvider is ViewPreloadSizeProvider) {
            val image = vh.getView<ImageView>(preloadId)
            image?.let {
                sizeProvider.setView(it)
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