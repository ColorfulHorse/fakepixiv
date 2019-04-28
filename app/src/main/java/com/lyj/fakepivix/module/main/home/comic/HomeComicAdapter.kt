package com.lyj.fakepivix.module.main.home.comic

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
import com.lyj.fakepivix.databinding.ItemHomeComicBinding
import com.lyj.fakepivix.databinding.ItemHomeIllustBinding
import com.lyj.fakepivix.module.main.home.illust.PixivisionHeader

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc
 */
class HomeComicAdapter(data: ObservableList<Illust>, val header: PixivisionHeader) : BaseMultiBindingAdapter<Illust>(data), ListPreloader.PreloadModelProvider<Illust> {
    var viewPreloadSizeProvider: ViewPreloadSizeProvider<Illust>? = null

    companion object {
        const val TYPE_ARTICLE_VIEW = 200
    }

    init {
        addItemType(Illust.TYPE_COMIC, R.layout.item_home_comic, BR.data)
    }



    override fun getItemViewType(position: Int): Int {
        val pos = position - headerLayoutCount
        if (pos == 2) {
            return TYPE_ARTICLE_VIEW
        }
        return super.getItemViewType(position)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<ViewDataBinding> {
        if (viewType == TYPE_ARTICLE_VIEW) {
            if (header.mBinding != null) {
                header.viewModel.load(header.category)
                return BaseBindingViewHolder(header.mBinding.root)
            }
        }
        val vh = super.onCreateViewHolder(parent, viewType)
        viewPreloadSizeProvider?.let {
            vh.binding?.let {
                binding ->
                if (binding is ItemHomeComicBinding) {
                    it.setView(binding.image)
                }
            }
        }
        return vh
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<ViewDataBinding>, position: Int) {
        if (holder.itemViewType == TYPE_ARTICLE_VIEW) {
            return
        }
        super.onBindViewHolder(holder, position)
    }

    override fun isFixedViewType(type: Int): Boolean {
        if (type == TYPE_ARTICLE_VIEW) {
            return true
        }
        return super.isFixedViewType(type)
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