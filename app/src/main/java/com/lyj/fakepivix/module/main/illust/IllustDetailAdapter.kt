package com.lyj.fakepivix.module.main.illust

import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.util.Log
import android.view.View
import android.view.ViewGroup
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
import com.lyj.fakepivix.module.main.home.illust.HomeIllustAdapter
import timber.log.Timber

/**
 * @author greensun
 *
 * @date 2019/5/30
 *
 * @desc 详情页adapter
 */
class IllustDetailAdapter(val viewModel: IllustDetailViewModel) : PreloadMultiBindingAdapter<Illust>(viewModel.data) {

    companion object {
        const val LAYOUT_DESC = 111
        const val LAYOUT_USER = 222
        const val LAYOUT_COMMENT = 333
        const val LAYOUT_RELATED_CAPTION = 444
    }

    var start = data.size

    var descFooter: DescFooter? = null
    var userFooter: UserFooter? = null
    var commentFooter: CommentFooter? = null
    var relatedCaptionFooter: RelatedCaptionFooter? = null

    init {
        addItemType(Illust.TYPE_LARGE, R.layout.item_illust_detail, BR.data)
        addItemType(Illust.TYPE_ILLUST, R.layout.item_home_illust, BR.illust)
        addItemType(Illust.TYPE_COMIC, R.layout.item_home_illust, BR.illust)
        addItemType(Illust.TYPE_NOVEL, R.layout.item_home_illust, BR.illust)
    }

    override fun getItemViewType(position: Int): Int {
        when {
            position == start -> {
                return LAYOUT_DESC
            }
            position == start + 1 -> {
                return LAYOUT_USER
            }
            position == start + 2 -> {
                return LAYOUT_COMMENT
            }
            position == start + 3 -> {
                return LAYOUT_RELATED_CAPTION
            }
            position > start + 3 -> {
                return super.getItemViewType(position - 4)
            }
            else -> {
                return super.getItemViewType(position)
            }
        }
    }

    override fun getItemCount(): Int {
        if (!data.isNullOrEmpty()) {
            return super.getItemCount() + 4
        }
        return super.getItemCount()
    }

    override fun getItem(position: Int): Illust? {
        Log.e("xxx","getItem：$position")
        if (position > start + 3) {
            return super.getItem(position - 4)
        }
        return super.getItem(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<ViewDataBinding> {
        when(viewType) {
            LAYOUT_DESC -> {
                descFooter?.let {
                    return BaseBindingViewHolder(it.rootView)
                }
            }
            LAYOUT_USER -> {
                userFooter?.let {
                    it.viewModel.load()
                    return BaseBindingViewHolder(it.rootView)
                }
            }
            LAYOUT_COMMENT -> {
                commentFooter?.let {
                    it.viewModel.load()
                    return BaseBindingViewHolder(it.rootView)
                }
            }
            LAYOUT_RELATED_CAPTION -> {
                relatedCaptionFooter?.let {
                    viewModel.load()
                    return BaseBindingViewHolder(it.rootView)
                }
            }
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<ViewDataBinding>, position: Int) {
        when(holder.itemViewType) {
            LAYOUT_DESC, LAYOUT_USER, LAYOUT_COMMENT, LAYOUT_RELATED_CAPTION ->
                return
        }
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
        return super.isFixedViewType(type) or (type == Illust.TYPE_LARGE) or (type == LAYOUT_DESC) or (type == LAYOUT_USER) or (type == LAYOUT_COMMENT) or (type == LAYOUT_RELATED_CAPTION)
    }


    override fun getPreloadRequestBuilder(item: Illust): RequestBuilder<*>? =
            GlideApp.with(mContext)
                    .load(item.image_urls.large)

}