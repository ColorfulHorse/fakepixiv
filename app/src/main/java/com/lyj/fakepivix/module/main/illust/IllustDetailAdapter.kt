package com.lyj.fakepivix.module.main.illust

import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import com.bumptech.glide.RequestBuilder
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.GlideApp
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingViewHolder
import com.lyj.fakepivix.app.adapter.PreloadMultiBindingAdapter
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.model.response.TrendTag
import com.lyj.fakepivix.databinding.ItemHomeIllustBinding
import com.lyj.fakepivix.databinding.ItemIllustDetailBinding

/**
 * @author greensun
 *
 * @date 2019/5/30
 *
 * @desc 详情页adapter
 */
class IllustDetailAdapter(val data: ObservableList<Illust>) : PreloadMultiBindingAdapter<Illust>(data) {

    init {
        addItemType(Illust.TYPE_LARGE, R.layout.item_illust_detail, BR.data)
        addItemType(Illust.TYPE_ILLUST, R.layout.item_home_illust, BR.illust)
        addItemType(Illust.TYPE_COMIC, R.layout.item_home_illust, BR.illust)
        addItemType(Illust.TYPE_NOVEL, R.layout.item_home_illust, BR.illust)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<ViewDataBinding>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.binding?.let {
            binding ->
            if (binding is ItemIllustDetailBinding) {
                sizeProvider.setView(binding.cover)
            }else if (binding is ItemHomeIllustBinding) {
                sizeProvider.setView(binding.image)
            }
        }
    }

    override fun isFixedViewType(type: Int): Boolean {
        return super.isFixedViewType(type) or (type == Illust.TYPE_LARGE)
    }


    override fun getPreloadRequestBuilder(item: Illust): RequestBuilder<*>? =
            GlideApp.with(mContext)
                    .load(item.image_urls.large)

}