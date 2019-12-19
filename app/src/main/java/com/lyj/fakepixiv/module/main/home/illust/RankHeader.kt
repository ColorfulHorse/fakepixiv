package com.lyj.fakepixiv.module.main.home.illust

import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import android.view.LayoutInflater
import androidx.databinding.library.baseAdapters.BR
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.constant.IllustCategory.*


import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.utils.Router
import com.lyj.fakepixiv.app.utils.dp2px
import com.lyj.fakepixiv.databinding.HeaderRankBinding
import com.lyj.fakepixiv.module.common.adapter.IllustAdapter
import com.lyj.fakepixiv.widget.CommonItemDecoration

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
            ILLUST, COMIC -> {
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
                readMore.setOnClickListener {
                    Router.goRank(category)
                }
            }
        }
    }
}