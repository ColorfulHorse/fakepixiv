package com.lyj.fakepivix.module.main.home.illust

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.util.ViewPreloadSizeProvider
import com.lyj.fakepivix.GlideApp
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingViewHolder
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.databinding.OnPropertyChangedCallbackImp
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.app.utils.attachLoadMore
import com.lyj.fakepivix.app.utils.dp2px
import com.lyj.fakepivix.databinding.CommonRefreshList
import com.lyj.fakepivix.databinding.ItemHomeIllustBinding
import com.lyj.fakepivix.widget.CommonItemDecoration


/**
 * @author greensun
 *
 * @date 2019/4/3
 *
 * @desc
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
        layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
        val pixivisionViewModel = mViewModel.pixivisionViewModel
        lifecycle.addObserver(pixivisionViewModel)
        // 特辑列表
        pixivisionHeader = PixivisionHeader(context, pixivisionViewModel)
        mAdapter = HomeIllustAdapter(mViewModel.data, pixivisionHeader)
        mAdapter.emptyView = layoutInflater.inflate(R.layout.layout_common_loading, null)
        with(mBinding) {
            initHeader()
            recyclerView.layoutManager = layoutManager
            mAdapter.bindToRecyclerView(recyclerView)
            recyclerView.addItemDecoration(CommonItemDecoration.Builder()
                    .draw(false)
                    .verticalWidth(3.5f.dp2px())
                    .build())
            recyclerView.attachLoadMore{ mViewModel.loadMore() }
//            recyclerView.setItemViewCacheSize(0)
//            // 回收时取消
            recyclerView.setRecyclerListener {
                if (it is BaseBindingViewHolder<*>) {
                    it.binding?.let {
                        binding ->
                        if (binding is ItemHomeIllustBinding) {
                            GlideApp.with(this@HomeIllustFragment).clear(binding.image)
                        }
                    }
                }
            }

            val sizeProvider = ViewPreloadSizeProvider<Illust>()
            mAdapter.viewPreloadSizeProvider = sizeProvider
            val recyPreloader = RecyclerViewPreloader<Illust>(this@HomeIllustFragment, mAdapter, sizeProvider, 8)
            recyclerView.addOnScrollListener(recyPreloader)
            refreshLayout.isEnabled = false
            refreshLayout.setOnRefreshListener {
                mViewModel.refresh()
            }

            with(mViewModel) {
                loadState.addOnPropertyChangedCallback(OnPropertyChangedCallbackImp {
                    _, _ ->
                    when(loadState.get()) {
                        is LoadState.Loading -> {
                            refreshLayout.isRefreshing = false
                            refreshLayout.isEnabled = false
                        }
                        else -> {
                            refreshLayout.isEnabled = true
                        }
                    }
                })
            }
        }
    }

    private fun initHeader() {
        val title = layoutInflater.inflate(R.layout.header_recommend, null)
        lifecycle.addObserver(mViewModel.liveViewModel)
        rankHeader = RankHeader(context, mViewModel.rankViewModel)
        liveHeader = LiveHeader(context, mViewModel.liveViewModel)
        mAdapter.addHeaderView(rankHeader.mBinding?.root)
        mAdapter.addHeaderView(liveHeader.mBinding?.root)
        mAdapter.addHeaderView(title)
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        mViewModel.lazyLoad()
    }

    override fun immersionBarEnabled(): Boolean = false

    override fun bindLayout(): Int = R.layout.layout_common_recycler

}