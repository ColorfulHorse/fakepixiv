package com.lyj.fakepixiv.module.main.search.illust

import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.RequestBuilder
import com.lyj.fakepixiv.BR
import com.lyj.fakepixiv.GlideApp
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.adapter.BaseBindingViewHolder
import com.lyj.fakepixiv.app.adapter.PreloadMultiBindingAdapter
import com.lyj.fakepixiv.app.data.model.response.TrendTag
import com.lyj.fakepixiv.app.utils.Router
import com.lyj.fakepixiv.databinding.SearchTagItem

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
        setOnItemClickListener { baseQuickAdapter, view, i ->
            Router.goSearch(mData[i].illust.type, mData[i].tag)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<ViewDataBinding> {
        val vh = super.onCreateViewHolder(parent, viewType)
        if (viewType == TrendTag.TYPE_NORMAL) {
            val image = vh.getView<ImageView>(R.id.image)
            image?.let {
                sizeProvider.setView(it)
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