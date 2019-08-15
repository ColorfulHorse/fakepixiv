package com.lyj.fakepivix.module.common.adapter

import android.databinding.ViewDataBinding
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.RequestBuilder
import com.lyj.fakepivix.GlideApp
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingViewHolder
import com.lyj.fakepivix.app.adapter.PreloadMultiBindingAdapter
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.utils.Router

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc
 */
open class IllustAdapter(data: MutableList<Illust>) : PreloadMultiBindingAdapter<Illust>(data) {

    init {
        setOnItemClickListener { _, _, position ->
            Router.goDetail(getRealPosition(position), mData)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<ViewDataBinding> {
        val vh = super.onCreateViewHolder(parent, viewType)
        val image = vh.getView<ImageView>(R.id.image)
        image?.let {
            sizeProvider.setView(it)
        }
        return vh
    }


    override fun isFixedViewType(type: Int): Boolean {
        return super.isFixedViewType(type) or (type == Illust.TYPE_META)
    }


    override fun getPreloadRequestBuilder(item: Illust): RequestBuilder<Drawable>? =
            GlideApp.with(mContext)
            .load(item.image_urls.medium)
}