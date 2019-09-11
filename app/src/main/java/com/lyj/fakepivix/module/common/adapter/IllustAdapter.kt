package com.lyj.fakepivix.module.common.adapter

import android.databinding.ObservableField
import android.databinding.ViewDataBinding
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.RequestBuilder
import com.lyj.fakepivix.GlideApp
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingViewHolder
import com.lyj.fakepivix.app.adapter.PreloadMultiBindingAdapter
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.databinding.bindAction
import com.lyj.fakepivix.app.databinding.onPropertyChangedCallback
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.app.utils.Router
import com.lyj.fakepivix.widget.LikeButton

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc
 */
open class IllustAdapter(data: MutableList<Illust>, val likeButton: Boolean = true) : PreloadMultiBindingAdapter<Illust>(data) {

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

    override fun convert(helper: BaseBindingViewHolder<ViewDataBinding>, item: Illust) {
        super.convert(helper, item)
        val button = helper.getView<LikeButton>(R.id.like)
        val series = helper.getView<View>(R.id.series_title)
        button?.let {
            if (!likeButton) it.visibility = View.GONE
            it.bindAction(item)
        }
        series?.let {
            series.setOnClickListener {
                // 系列
            }
        }
    }


    override fun isFixedViewType(type: Int): Boolean {
        return super.isFixedViewType(type) or (type == Illust.TYPE_META)
    }


    override fun getPreloadRequestBuilder(item: Illust): RequestBuilder<Drawable>? =
            GlideApp.with(mContext)
            .load(item.image_urls.medium)
}