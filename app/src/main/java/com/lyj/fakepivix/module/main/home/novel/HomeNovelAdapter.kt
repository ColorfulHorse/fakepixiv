package com.lyj.fakepivix.module.main.home.novel

import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.util.ViewPreloadSizeProvider
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.GlideApp
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingViewHolder
import com.lyj.fakepivix.app.adapter.BaseMultiBindingAdapter
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.utils.mapUrl
import com.lyj.fakepivix.databinding.ItemHomeIllustBinding
import com.lyj.fakepivix.databinding.ItemHomeNovelBinding
import com.lyj.fakepivix.module.main.home.illust.PixivisionHeader

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc
 */
class HomeNovelAdapter(data: ObservableList<Illust>) : BaseMultiBindingAdapter<Illust>(data), ListPreloader.PreloadModelProvider<Illust> {
    var viewPreloadSizeProvider: ViewPreloadSizeProvider<Illust>? = null


    init {
        addItemType(Illust.TYPE_NOVEL, R.layout.item_home_novel, BR.data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<ViewDataBinding> {
        val vh = super.onCreateViewHolder(parent, viewType)
        viewPreloadSizeProvider?.let {
            vh.binding?.let {
                binding ->
                if (binding is ItemHomeNovelBinding) {
                    it.setView(binding.cover)
                }
            }
        }
        return vh
    }

    override fun getPreloadItems(position: Int): MutableList<Illust> {
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


    override fun getPreloadRequestBuilder(item: Illust): RequestBuilder<Drawable>? = GlideApp.with(recyclerView)
            .load(item.image_urls.medium.mapUrl())

}