package com.lyj.fakepivix.module.main.home.illust

import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.GlideApp
import com.lyj.fakepivix.R
import com.lyj.fakepivix.R.id.count
import com.lyj.fakepivix.app.adapter.BaseBindingViewHolder
import com.lyj.fakepivix.app.adapter.BaseMultiBindingAdapter
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.databinding.url
import com.lyj.fakepivix.app.utils.mapUrl
import com.lyj.fakepivix.databinding.HeaderPixivisionBinding

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc
 */
class HomeIllustAdapter(data: ObservableList<Illust>, val header: PixivisionHeader) : BaseMultiBindingAdapter<Illust>(data), ListPreloader.PreloadModelProvider<Illust> {
    private val preLoadCount = 4

    companion object {
        const val TYPE_ARTICLE_VIEW = 200
    }

    init {
        addItemType(Illust.TYPE_ILLUST, R.layout.item_home_illust, BR.illust)
    }



    override fun getItemViewType(position: Int): Int {
        val pos = position - headerLayoutCount
        if (pos == 10) {
            return TYPE_ARTICLE_VIEW
        }
        return super.getItemViewType(position)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<ViewDataBinding> {
        if (viewType == TYPE_ARTICLE_VIEW) {
            if (header.mBinding != null) {
                header.viewModel.load()
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


    override fun getPreloadItems(position: Int): MutableList<Illust> {
        if (data.isNotEmpty()) {
            var end = position + preLoadCount
            if (end >= data.size) {
                end = data.size - 1
            }
            if (end > position) {
                Log.e("preload", "start$position=====end$end")
                return data.subList(position, end)
            }
        }
        return mutableListOf()
    }


    override fun getPreloadRequestBuilder(item: Illust): RequestBuilder<*>? = GlideApp.with(mContext)
            .load(item.image_urls.square_medium.mapUrl())


}