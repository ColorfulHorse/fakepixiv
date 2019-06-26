package com.lyj.fakepivix.module.main.common.adapter

import android.app.Activity
import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.graphics.drawable.Drawable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.ViewGroup
import com.bumptech.glide.RequestBuilder
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.GlideApp
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingViewHolder
import com.lyj.fakepivix.app.adapter.PreloadMultiBindingAdapter
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.databinding.ItemHomeIllustBinding
import com.lyj.fakepivix.module.main.illust.IllustDetailRootFragment
import me.yokeyword.fragmentation.SupportHelper

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc
 */
open class IllustAdapter(data: ObservableList<Illust>) : PreloadMultiBindingAdapter<Illust>(data) {
    init {
        addItemType(Illust.TYPE_ILLUST, R.layout.item_home_illust, BR.illust)

        setOnItemClickListener { _, _, position ->
            IllustRepository.instance.illustList = data
            val top = SupportHelper.getTopFragment((mContext as FragmentActivity).supportFragmentManager) as FragmentationFragment<*, *>
            top.start(IllustDetailRootFragment.newInstance(position))
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<ViewDataBinding> {
        val vh = super.onCreateViewHolder(parent, viewType)
        vh.binding?.let {
            binding ->
            if (binding is ItemHomeIllustBinding) {
                sizeProvider.setView(binding.image)
            }
        }
        return vh
    }

    override fun isFixedViewType(type: Int): Boolean {
        return super.isFixedViewType(type) or (type == Illust.TYPE_LARGE)
    }


    override fun getPreloadRequestBuilder(item: Illust): RequestBuilder<Drawable>? =
            GlideApp.with(mContext)
            .load(item.image_urls.medium)
}