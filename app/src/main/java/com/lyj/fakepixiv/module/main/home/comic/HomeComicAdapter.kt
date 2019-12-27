package com.lyj.fakepixiv.module.main.home.comic

import androidx.databinding.ObservableList
import androidx.databinding.ViewDataBinding
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.adapter.BaseBindingViewHolder
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.module.common.adapter.IllustAdapter
import com.lyj.fakepixiv.module.main.home.illust.PixivisionHeader

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc
 */
class HomeComicAdapter(data: ObservableList<Illust>, val header: PixivisionHeader) : IllustAdapter(data) {

    companion object {
        const val TYPE_ARTICLE_VIEW = 200
    }


    init {
        addItemType(Illust.TYPE_ILLUST, R.layout.item_home_comic, BR.data)
        addItemType(Illust.TYPE_COMIC, R.layout.item_home_comic, BR.data)
    }

    override fun getRealPosition(position: Int): Int {
        if (position > 2) {
            return position - 1
        }
        return super.getRealPosition(position)
    }


    /**
     * 由于中间插入了item
     */
    override fun getItemViewType(position: Int): Int {
        val pos = position - headerLayoutCount
        if (pos == 2) {
            return TYPE_ARTICLE_VIEW
        }
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        if (!mData.isNullOrEmpty()) {
            return super.getItemCount() + 1
        }
        return super.getItemCount()
    }

    override fun getItem(position: Int): Illust? {
        if (position > 2) {
            return super.getItem(position - 1)
        }
        return super.getItem(position)
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
}