package com.lyj.fakepivix.module.main.home.illust

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.view.LayoutInflater
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.constant.IllustCategory.ILLUST
import com.lyj.fakepivix.app.constant.IllustCategory.NOVEL


import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.utils.dp2px
import com.lyj.fakepivix.databinding.HeaderRankBinding
import com.lyj.fakepivix.module.common.adapter.IllustAdapter
import com.lyj.fakepivix.widget.CommonItemDecoration

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc 排行榜list
 */
class RankHeader(val context: Context?, viewModel: RankViewModel, @IllustCategory val category: String = ILLUST) {

    private val rootView = LayoutInflater.from(context).inflate(R.layout.header_rank, null)

    val mBinding: HeaderRankBinding? = DataBindingUtil.bind(rootView)

    val adapter: IllustAdapter = IllustAdapter(viewModel.data)

    init {
        when(category) {
            ILLUST -> {
                adapter.addItemType(Illust.TYPE_ILLUST, R.layout.item_home_rank_illust, BR.data)
                adapter.addItemType(Illust.TYPE_COMIC, R.layout.item_home_rank_illust, BR.data)
            }
            NOVEL -> {
                adapter.addItemType(Illust.TYPE_NOVEL, R.layout.item_home_rank_novel, BR.data)
                adapter.addItemType(Illust.TYPE_ILLUST, R.layout.item_home_rank_novel, BR.data)
            }
            else -> {
                adapter.addItemType(Illust.TYPE_ILLUST, R.layout.item_home_rank_illust, BR.data)
            }
        }
        if (mBinding != null) {
            with(mBinding) {
                val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                recyclerView.layoutManager = layoutManager
                recyclerView.addItemDecoration(CommonItemDecoration.Builder()
                        .draw(false)
                        .edge(16.dp2px(), 5.dp2px())
                        .dividerWidth(10.dp2px(), 10.dp2px())
                        .build())
                PagerSnapHelper().attachToRecyclerView(recyclerView)
                recyclerView.adapter = adapter
            }
        }
    }
}