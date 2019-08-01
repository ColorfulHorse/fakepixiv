package com.lyj.fakepivix.module.main.home.illust

import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.view.ViewGroup
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingViewHolder
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.utils.Router
import com.lyj.fakepivix.module.common.adapter.IllustAdapter

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

    init {
        addItemType(Illust.TYPE_ILLUST, R.layout.item_home_illust, BR.illust)
        setOnItemClickListener { _, _, position ->
            var pos = position
            if (position > 10) {
                pos = position - 1
            }
            Router.goDetail(pos, data)
        }
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
        if (!data.isNullOrEmpty()) {
            return super.getItemCount() + 1
        }
        return super.getItemCount()
    }

    override fun getItem(position: Int): Illust? {
        if (position > 10) {
            return super.getItem(position - 1)
        }
        return super.getItem(position)
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