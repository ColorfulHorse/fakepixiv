package com.lyj.fakepixiv.module.illust.ranking

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration
import com.chad.library.adapter.base.BaseQuickAdapter
import com.lyj.fakepixiv.BR
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.application.ApplicationLike.Companion.context
import com.lyj.fakepixiv.app.adapter.BaseBindingAdapter
import com.lyj.fakepixiv.app.adapter.BaseBindingViewHolder
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.data.model.response.Tag
import com.lyj.fakepixiv.app.utils.Router
import com.lyj.fakepixiv.app.utils.dp2px
import com.lyj.fakepixiv.databinding.ItemTagBinding
import com.lyj.fakepixiv.module.common.adapter.IllustAdapter

/**
 * @author greensun
 *
 * @date 2019/9/4
 *
 * @desc 排行榜
 */
class RankingAdapter(data: MutableList<Illust>, likeButton: Boolean = true) : IllustAdapter(data, likeButton) {

    init {
        setOnItemChildClickListener { baseQuickAdapter, view, i ->
            Router.goUserDetail(data[i].user)
        }
    }

    override fun isFixedViewType(type: Int): Boolean {
        return super.isFixedViewType(type) || (type and Illust.TYPE_RANK == Illust.TYPE_RANK)
    }

    override fun convert(helper: BaseBindingViewHolder<ViewDataBinding>, item: Illust) {
        super.convert(helper, item)
        var pos = helper.layoutPosition - headerLayoutCount
        if (pos in 0..2) {
            var margin = 6.dp2px()
            var b = 0
            if (pos == 2) {
                b = margin
            }
            val lp = helper.itemView.layoutParams as ViewGroup.MarginLayoutParams
            lp.setMargins(margin, margin, margin, b)
            helper.itemView.layoutParams = lp
        }
        if(item.itemType == Illust.TYPE_RANK + Illust.TYPE_NOVEL) {
            val data = item.tags.toMutableList()
            val recyclerView = helper.getView<RecyclerView>(R.id.rv_tag)
            var adapter = recyclerView.adapter
            if (adapter == null) {
                adapter = BaseBindingAdapter<Tag, ItemTagBinding>(R.layout.item_rank_tag, data, BR.data)
                recyclerView.layoutManager = ChipsLayoutManager.newBuilder(context)
                        .setOrientation(ChipsLayoutManager.HORIZONTAL)
                        .setRowStrategy(ChipsLayoutManager.STRATEGY_CENTER_DENSE)
                        .withLastRow(true)
                        .build()
                recyclerView.adapter = adapter
                recyclerView.addItemDecoration(SpacingItemDecoration(1.dp2px(), 8.dp2px()))
            }else {
                (adapter as BaseQuickAdapter<Tag, *>).setNewData(data)
            }
        }
        if (Illust.TYPE_RANK == (Illust.TYPE_RANK and item.itemType)) {
            helper.addOnClickListener(R.id.avatar)
        }
    }
}