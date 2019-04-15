package com.lyj.fakepivix.module.main.home.illust

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingAdapter
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.utils.dp2px
import com.lyj.fakepivix.databinding.HeaderRankBinding
import com.lyj.fakepivix.databinding.ItemHomeRankIllustBinding
import com.lyj.fakepivix.widget.CommonItemDecoration

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc
 */
class RankHeader(val context: Context?, val viewModel: RankViewModel) {

    private val rootView = LayoutInflater.from(context).inflate(R.layout.header_rank, null)

    val mBinding: HeaderRankBinding? = DataBindingUtil.bind(rootView)

    val adapter: BaseBindingAdapter<Illust, ItemHomeRankIllustBinding> = BaseBindingAdapter(R.layout.item_home_rank_illust, viewModel.data, BR.data)

    init {
        if (mBinding != null) {
            with(mBinding) {
                vm = viewModel
                val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                recyclerView.layoutManager = layoutManager
                recyclerView.addItemDecoration(CommonItemDecoration.Builder()
                        .draw(false)
                        .edge(false)
                        .horizontalWidth(10.dp2px())
                        .build())
                recyclerView.adapter = adapter
            }
        }
    }
}