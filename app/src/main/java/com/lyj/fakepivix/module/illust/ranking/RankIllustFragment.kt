package com.lyj.fakepivix.module.illust.ranking

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.utils.bindState
import com.lyj.fakepivix.app.utils.dp2px
import com.lyj.fakepivix.databinding.CommonList
import com.lyj.fakepivix.module.common.IllustListFragment
import com.lyj.fakepivix.module.common.IllustListViewModel
import com.lyj.fakepivix.widget.CommonItemDecoration

/**
 * @author greensun
 *
 * @date 2019/9/2
 *
 * @desc 排行榜列表fragment
 */
class RankIllustFragment : FragmentationFragment<CommonList, IllustListViewModel?>() {

    override var mViewModel: IllustListViewModel? = null

    var category = IllustCategory.ILLUST

    companion object {
        const val EXTRA_CATEGORY = "EXTRA_CATEGORY"
        fun newInstance(@IllustCategory category: String) = RankIllustFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_CATEGORY, category)
            }
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        arguments?.let {
            category = it.getString(EXTRA_CATEGORY, IllustCategory.ILLUST)
        }
        mViewModel?.let {
            val adapter = RankingAdapter(it.data)
            var layoutManager = LinearLayoutManager(mActivity)
            when(category) {
                IllustCategory.ILLUST, IllustCategory.OTHER, IllustCategory.COMIC -> {
                    layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
                    mBinding.recyclerView.addItemDecoration(CommonItemDecoration.Builder()
                            .dividerWidth(3.5f.dp2px(), 3.5f.dp2px())
                            .build())
                }
                IllustCategory.NOVEL -> {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    mBinding.recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
                }
            }
            adapter.apply {
                addItemType(Illust.TYPE_RANK + Illust.TYPE_ILLUST, R.layout.item_rank_illust, BR.data)
                addItemType(Illust.TYPE_RANK + Illust.TYPE_COMIC, R.layout.item_rank_illust, BR.data)
                addItemType(Illust.TYPE_RANK + Illust.TYPE_NOVEL, R.layout.item_rank_novel, BR.data)
                addItemType(Illust.TYPE_ILLUST, R.layout.item_home_illust, BR.data)
                addItemType(Illust.TYPE_COMIC, R.layout.item_home_comic, BR.data)
                addItemType(Illust.TYPE_NOVEL, R.layout.item_home_novel, BR.data)
            }
            mBinding.recyclerView.layoutManager = layoutManager
            adapter.bindToRecyclerView(mBinding.recyclerView)
            adapter.bindState(it.loadState) {
                it.load()
            }
        }
        mViewModel?.load()
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
    }

    override fun immersionBarEnabled(): Boolean = false

    override fun bindLayout(): Int = R.layout.layout_common_recycler
}