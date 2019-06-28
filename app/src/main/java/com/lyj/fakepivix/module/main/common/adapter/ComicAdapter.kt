package com.lyj.fakepivix.module.main.common.adapter

import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import com.bumptech.glide.RequestBuilder
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.GlideApp
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingViewHolder
import com.lyj.fakepivix.app.adapter.PreloadMultiBindingAdapter
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.utils.Router
import com.lyj.fakepivix.databinding.ItemHomeComicBinding

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc
 */
@Deprecated("合并为IllustAdapter")
open class ComicAdapter(data: ObservableList<Illust>) : PreloadMultiBindingAdapter<Illust>(data) {

    init {
        addItemType(Illust.TYPE_COMIC, R.layout.item_home_comic, BR.data)
        setOnItemClickListener { _, _, position ->
            Router.goDetail(position, data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<ViewDataBinding> {
        val vh = super.onCreateViewHolder(parent, viewType)
        vh.binding?.let { binding ->
            if (binding is ItemHomeComicBinding) {
                sizeProvider.setView(binding.image)
            }
        }
        return vh
    }

    override fun getPreloadRequestBuilder(item: Illust): RequestBuilder<Drawable>? = GlideApp.with(recyclerView)
            .load(item.image_urls.medium)

}