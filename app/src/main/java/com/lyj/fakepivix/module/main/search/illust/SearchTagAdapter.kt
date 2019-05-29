package com.lyj.fakepivix.module.main.search.illust

import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.view.ViewGroup
import com.bumptech.glide.RequestBuilder
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.GlideApp
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingViewHolder
import com.lyj.fakepivix.app.adapter.PreloadMultiBindingAdapter
import com.lyj.fakepivix.app.data.model.response.TrendTag
import com.lyj.fakepivix.databinding.SearchTagItem

/**
 * @author greensun
 *
 * @date 2019/5/29
 *
 * @desc 标签搜索adapter
 */
class SearchTagAdapter(val data: ObservableList<TrendTag>) : PreloadMultiBindingAdapter<TrendTag>(data) {

    init {
        addItemType(TrendTag.TYPE_HEADER, R.layout.item_header_search_tag, BR.tag)
        addItemType(TrendTag.TYPE_NORMAL, R.layout.item_search_tag, BR.tag)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<ViewDataBinding> {
        val vh = super.onCreateViewHolder(parent, viewType)
        vh.binding?.let {
            binding ->
            if (binding is SearchTagItem) {
                sizeProvider.setView(binding.cover)
            }
        }
        return vh
    }

    override fun isFixedViewType(type: Int): Boolean {
        return super.isFixedViewType(type) or (type == TrendTag.TYPE_HEADER)
    }

    override fun getPreloadRequestBuilder(item: TrendTag): RequestBuilder<*>? = GlideApp.with(mContext)
            .load(item.illust.image_urls.medium)
}