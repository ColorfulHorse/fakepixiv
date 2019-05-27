package com.lyj.fakepivix.module.main.home.illust

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.view.LayoutInflater
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingAdapter
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.constant.IllustCategory.ILLUST
import com.lyj.fakepivix.app.constant.IllustCategory.NOVEL


import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.utils.dp2px
import com.lyj.fakepivix.databinding.HeaderRankBinding
import com.lyj.fakepivix.widget.CommonItemDecoration

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc 插画排行榜list
 */
class RankHeader(val context: Context?, viewModel: RankViewModel, @IllustCategory val category: String = ILLUST) {

    private val rootView = LayoutInflater.from(context).inflate(R.layout.header_rank, null)

    val mBinding: HeaderRankBinding? = DataBindingUtil.bind(rootView)

    val adapter: BaseBindingAdapter<Illust, ViewDataBinding>

    init {
        val layoutId = when(category) {
            ILLUST -> R.layout.item_home_rank_illust
            NOVEL -> R.layout.item_home_rank_novel
            else -> R.layout.item_home_rank_illust
        }
        adapter = BaseBindingAdapter(layoutId, viewModel.data, BR.data)
        if (mBinding != null) {
            with(mBinding) {
                val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                recyclerView.layoutManager = layoutManager
                recyclerView.addItemDecoration(CommonItemDecoration.Builder()
                        .draw(false)
                        .edge(16.dp2px(), 5.dp2px())
                        .horizontalWidth(10.dp2px())
                        .build())
                PagerSnapHelper().attachToRecyclerView(recyclerView)
                recyclerView.adapter = adapter
            }
        }
    }
}