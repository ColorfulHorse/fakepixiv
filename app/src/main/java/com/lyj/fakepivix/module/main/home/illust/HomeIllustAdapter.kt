package com.lyj.fakepivix.module.main.home.illust

import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import com.bumptech.glide.RequestBuilder
import com.lyj.fakepivix.GlideApp
import com.lyj.fakepivix.app.adapter.BaseBindingViewHolder
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.utils.mapUrl
import com.lyj.fakepivix.module.main.common.adapter.IllustAdapter

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc
 */
class HomeIllustAdapter(data: ObservableList<Illust>, val header: PixivisionHeader) : IllustAdapter(data) {

    companion object {
        const val TYPE_ARTICLE_VIEW = 200
    }

    override fun getItemViewType(position: Int): Int {
        val pos = position - headerLayoutCount
        if (pos == 10) {
            return TYPE_ARTICLE_VIEW
        }else if (pos > 10) {
            return super.getItemViewType(position - 1)
        }
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<ViewDataBinding> {
        if (viewType == TYPE_ARTICLE_VIEW) {
            if (header.mBinding != null) {
                header.viewModel.load(header.category)
                return BaseBindingViewHolder(header.mBinding.root)
            }
        }
        return super.onCreateViewHolder(parent, viewType)
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

}