package com.lyj.fakepivix.module.illust.detail

import android.databinding.ViewDataBinding
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.ViewGroup
import com.bumptech.glide.RequestBuilder
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.GlideApp
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingViewHolder
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.utils.Router
import com.lyj.fakepivix.module.common.adapter.IllustAdapter
import com.lyj.fakepivix.module.illust.detail.items.*
import com.lyj.fakepivix.module.illust.detail.items.DetailItem.Companion.LAYOUT_COMMENT
import com.lyj.fakepivix.module.illust.detail.items.DetailItem.Companion.LAYOUT_DESC
import com.lyj.fakepivix.module.illust.detail.items.DetailItem.Companion.LAYOUT_RELATED_CAPTION
import com.lyj.fakepivix.module.illust.detail.items.DetailItem.Companion.LAYOUT_SERIES
import com.lyj.fakepivix.module.illust.detail.items.DetailItem.Companion.LAYOUT_USER
import timber.log.Timber


/**
 * @author greensun
 *
 * @date 2019/5/30
 *
 * @desc 详情页adapter
 */
class IllustDetailAdapter(val viewModel: IllustDetailViewModel) : IllustAdapter(viewModel.data) {

    var start = data.size

    val items = mutableListOf<DetailItem>()


    init {
        addItemType(Illust.TYPE_META, R.layout.item_illust_detail, BR.data)
        addItemType(Illust.TYPE_ILLUST, R.layout.item_home_illust, BR.illust)
        addItemType(Illust.TYPE_COMIC, R.layout.item_home_illust, BR.illust)
        addItemType(Illust.TYPE_NOVEL, R.layout.item_home_illust, BR.illust)

        setOnItemClickListener { _, _, position ->
            val type = data[position].itemType
            if (type == Illust.TYPE_META) {

            }else {
                Router.goDetail(position - items.size, data)
            }
        }
    }

    fun refreshStart() {
        start = data.size
    }

    fun addItem(item: DetailItem) {
        items.add(item)
    }


    override fun getItemViewType(position: Int): Int {
        when {
            position in start until start + items.size -> {
                return items[position - start].type
            }
            position > start + items.size -> {
                return super.getItemViewType(position - items.size)
            }
            else -> {
                return super.getItemViewType(position)
            }
        }
    }

    override fun getItemCount(): Int {
        if (!data.isNullOrEmpty()) {
            return super.getItemCount() + items.size
        }
        return super.getItemCount()
    }

    override fun getItem(position: Int): Illust? {
        if (position > start + 3) {
            return super.getItem(position - items.size)
        }
        return super.getItem(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<ViewDataBinding> {
        when(viewType) {
            LAYOUT_DESC -> {
                val item = items.find { it.type == LAYOUT_DESC } as DescFooter
                return BaseBindingViewHolder(item.rootView)
            }
            LAYOUT_SERIES -> {
                val item = items.find { it.type == LAYOUT_SERIES } as SeriesItem
                item.viewModel.load()
                return BaseBindingViewHolder(item.mBinding)
            }
            LAYOUT_USER -> {
                val item = items.find { it.type == LAYOUT_USER } as UserFooter
                Log.e("adapter", "${this} load item")
                item.viewModel.load()
                return BaseBindingViewHolder(item.rootView)
            }
            LAYOUT_COMMENT -> {
                val item = items.find { it.type == LAYOUT_COMMENT } as CommentFooter
                item.viewModel.load()
                return BaseBindingViewHolder(item.rootView)
            }
            LAYOUT_RELATED_CAPTION -> {
                val item = items.find { it.type == LAYOUT_RELATED_CAPTION } as RelatedCaptionFooter
                item.viewModel.parent.load()
                return BaseBindingViewHolder(item.rootView)
            }
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<ViewDataBinding>, position: Int) {
        when(holder.itemViewType) {
            LAYOUT_DESC, LAYOUT_SERIES, LAYOUT_USER, LAYOUT_COMMENT, LAYOUT_RELATED_CAPTION ->
                return
        }
        super.onBindViewHolder(holder, position)
    }

    override fun isFixedViewType(type: Int): Boolean {
        return super.isFixedViewType(type) or (type == Illust.TYPE_META) or (type == LAYOUT_DESC) or (type == LAYOUT_SERIES) or (type == LAYOUT_USER) or (type == LAYOUT_COMMENT) or (type == LAYOUT_RELATED_CAPTION)
    }


    override fun getPreloadRequestBuilder(item: Illust): RequestBuilder<Drawable>? =
            GlideApp.with(mContext)
                    .load(item.image_urls.large)

}