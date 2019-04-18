package com.lyj.fakepivix.module.main.home.illust

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.View
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.util.FixedPreloadSizeProvider
import com.bumptech.glide.util.ViewPreloadSizeProvider
import com.chad.library.adapter.base.BaseQuickAdapter
import com.flyco.tablayout.listener.CustomTabEntity
import com.lyj.fakepivix.GlideApp
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.App.Companion.context
import com.lyj.fakepivix.app.adapter.BaseBindingViewHolder
import com.lyj.fakepivix.app.adapter.BaseMultiBindingAdapter
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.FragmentationFragment;
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.entity.TabBean
import com.lyj.fakepivix.app.utils.dp2px
import com.lyj.fakepivix.databinding.*
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
        val pixivisionViewModel = PixivisionViewModel()
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
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        val adapter = recyclerView.adapter
                        adapter?.let {
                            val last = it.itemCount - 1
                            val pos = layoutManager.findLastVisibleItemPosition()
                            Log.e("scroll", "position:$pos")
                            if (pos < last) {
                                val layoutManager = recyclerView.layoutManager
                                if (layoutManager is LinearLayoutManager) {
                                    if (layoutManager.orientation == LinearLayoutManager.VERTICAL) {
                                        if (last - pos <= 10) {
                                            Log.e("scroll", "loadmore====position:$pos")
                                            mViewModel.loadMore()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            })


            recyclerView.setItemViewCacheSize(0)
            // 回收时取消
            recyclerView.setRecyclerListener {
                if (it is BaseBindingViewHolder<*>) {
                    it.binding?.let {
                        binding ->
                        if (binding is ItemHomeIllustBinding) {
                            GlideApp.with(mActivity).clear(binding.image)
                        }
                    }
                }
            }

            //val sizeProvider = ViewPreloadSizeProvider<Illust>()
            val sizeProvider = FixedPreloadSizeProvider<Illust>(180.dp2px(), 180.dp2px())
            val recyPreloader = RecyclerViewPreloader<Illust>(mActivity, mAdapter, sizeProvider, 8)
            recyclerView.addOnScrollListener(recyPreloader)
        }
    }

    private fun initHeader() {
        val title = layoutInflater.inflate(R.layout.header_recommend, null)
        lifecycle.addObserver(mViewModel.liveViewModel)
        //lifecycle.addObserver(mViewModel.pixivisionViewModel)
        rankHeader = RankHeader(context, mViewModel.rankViewModel)
        liveHeader = LiveHeader(context, mViewModel.liveViewModel)
       // pixivisionHeader = PixivisionHeader(context, mViewModel.pixivisionViewModel)
        mAdapter.addHeaderView(rankHeader.mBinding?.root)
        mAdapter.addHeaderView(liveHeader.mBinding?.root)
        mAdapter.addHeaderView(title)
        //mAdapter.addHeaderView(pixivisionHeader.mBinding?.root, 10)
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        mViewModel.lazyLoad()
    }

    override fun immersionBarEnabled(): Boolean = false

    override fun bindLayout(): Int = R.layout.layout_common_recycler

}