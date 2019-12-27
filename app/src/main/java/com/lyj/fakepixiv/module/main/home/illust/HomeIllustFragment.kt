package com.lyj.fakepixiv.module.main.home.illust

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.lyj.fakepixiv.GlideApp
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.adapter.BaseBindingViewHolder
import com.lyj.fakepixiv.app.base.FragmentationFragment
import com.lyj.fakepixiv.app.utils.attachLoadMore
import com.lyj.fakepixiv.app.utils.bindState
import com.lyj.fakepixiv.app.utils.dp2px
import com.lyj.fakepixiv.databinding.CommonRefreshList
import com.lyj.fakepixiv.databinding.ItemHomeIllustBinding
import com.lyj.fakepixiv.widget.CommonItemDecoration


/**
 * @author greensun
 *
 * @date 2019/4/3
 *
 * @desc 主页推荐-插画
 */
class HomeIllustFragment : FragmentationFragment<CommonRefreshList, HomeIllustViewModel>() {

    override var mViewModel = HomeIllustViewModel()

    companion object {
        fun newInstance() = HomeIllustFragment()
    }

    private lateinit var layoutManager: GridLayoutManager
    private lateinit var mAdapter: HomeIllustAdapter
    // 排行榜，直播，pixivision头部
    private lateinit var rankHeader: RankHeader
    private lateinit var liveHeader: LiveHeader
    private lateinit var pixivisionHeader: PixivisionHeader


    override fun init(savedInstanceState: Bundle?) {
        initList()
    }

    /**
     * 初始化列表
     */
    private fun initList() {
        layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
        val pixivisionViewModel = mViewModel.pixivisionViewModel
        // 特辑列表
        pixivisionHeader = PixivisionHeader(context, pixivisionViewModel)
        mAdapter = HomeIllustAdapter(mViewModel.data, pixivisionHeader)
        with(mBinding) {
            initHeader()
            recyclerView.layoutManager = layoutManager
            mAdapter.bindToRecyclerView(recyclerView)
            recyclerView.addItemDecoration(CommonItemDecoration.Builder()
                    .dividerWidth(0, 3.5f.dp2px())
                    .build())
            // 加载更多
            recyclerView.attachLoadMore(mViewModel.loadMoreState){ mViewModel.loadMore() }

            recyclerView.setRecyclerListener {
                if (it is BaseBindingViewHolder<*>) {
                    it.binding?.let { binding ->
                        if (binding is ItemHomeIllustBinding) {
                            GlideApp.with(this@HomeIllustFragment).clear(binding.image)
                        }
                    }
                }
            }

            mAdapter.bindState(mViewModel.loadState, refreshLayout = refreshLayout) {
                mViewModel.load()
            }

        }
    }

    /**
     * 初始化头部
     */
    private fun initHeader() {
        val title = layoutInflater.inflate(R.layout.header_recommend, null)
        rankHeader = RankHeader(context, mViewModel.rankViewModel)
        liveHeader = LiveHeader(context, mViewModel.liveViewModel)
        mAdapter.addHeaderView(rankHeader.mBinding?.root)
        mAdapter.addHeaderView(liveHeader.mBinding?.root)
        mAdapter.addHeaderView(title)
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        mViewModel.load()
    }

    override fun immersionBarEnabled(): Boolean = false

    override fun bindLayout(): Int = R.layout.layout_common_refresh_recycler

}