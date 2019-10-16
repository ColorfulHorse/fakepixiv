package com.lyj.fakepixiv.module.common.adapter

import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.RequestBuilder
import com.lyj.fakepixiv.BR
import com.lyj.fakepixiv.GlideApp
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.adapter.BaseBindingViewHolder
import com.lyj.fakepixiv.app.adapter.PreloadMultiBindingAdapter
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.utils.Router

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc
 */
@Deprecated("合并为IllustAdapter")
class NovelAdapter(data: ObservableList<Illust>) : PreloadMultiBindingAdapter<Illust>(data) {

    init {
        addItemType(Illust.TYPE_NOVEL, R.layout.item_home_novel, BR.data)
        addItemType(Illust.TYPE_ILLUST, R.layout.item_home_novel, BR.data)
        setOnItemClickListener { _, _, position ->
            Router.goDetail(position, data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<ViewDataBinding> {
        val vh = super.onCreateViewHolder(parent, viewType)
        val image = vh.getView<ImageView>(R.id.image)
        sizeProvider.setView(image)
        return vh
    }

    override fun getPreloadRequestBuilder(item: Illust): RequestBuilder<Drawable>? = GlideApp.with(recyclerView)
            .load(item.image_urls.medium)

}