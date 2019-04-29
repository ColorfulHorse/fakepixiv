package com.lyj.fakepivix.module.main.home.illust

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.util.ViewPreloadSizeProvider
import com.lyj.fakepivix.GlideApp
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingViewHolder
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.databinding.OnPropertyChangedCallbackImp
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.app.utils.ToastUtil
import com.lyj.fakepivix.app.utils.attachLoadMore
import com.lyj.fakepivix.app.utils.dp2px
import com.lyj.fakepivix.databinding.CommonRefreshList
import com.lyj.fakepivix.databinding.ItemHomeIllustBinding
import com.lyj.fakepivix.module.main.common.adapter.IllustAdapter
import com.lyj.fakepivix.widget.CommonItemDecoration
import kotlinx.android.synthetic.main.layout_error.view.*


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
    private lateinit var mAdapter: IllustAdapter
    // 排行榜，直播，pixivision头部
    private lateinit var rankHeader: RankHeader
    private lateinit var liveHeader: LiveHeader
    private lateinit var pixivisionHeader: PixivisionHeader

    private val loadingView: View by lazy { layoutInflater.inflate(R.layout.layout_common_loading, null) }
    private val errorView: View by lazy { layoutInflater.inflate(R.layout.layout_error, null) }

    override fun init(savedInstanceState: Bundle?) {
        initList()
        listenState()
    }

    /**
     * 初始化列表
     */
    private fun initList() {
        layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
        val pixivisionViewModel = mViewModel.pixivisionViewModel
        lifecycle.addObserver(pixivisionViewModel)
        // 特辑列表
        pixivisionHeader = PixivisionHeader(context, pixivisionViewModel)
        mAdapter = HomeIllustAdapter(mViewModel.data, pixivisionHeader)

        with(mBinding) {
            initHeader()
            recyclerView.layoutManager = layoutManager
            mAdapter.bindToRecyclerView(recyclerView)
            recyclerView.addItemDecoration(CommonItemDecoration.Builder()
                    .draw(false)
                    .verticalWidth(3.5f.dp2px())
                    .build())
            // 加载更多
            recyclerView.attachLoadMore { mViewModel.loadMore() }

            recyclerView.setRecyclerListener {
                if (it is BaseBindingViewHolder<*>) {
                    it.binding?.let { binding ->
                        if (binding is ItemHomeIllustBinding) {
                            GlideApp.with(this@HomeIllustFragment).clear(binding.image)
                        }
                    }
                }
            }

            // 刷新
            refreshLayout.setOnRefreshListener {
                mViewModel.lazyLoad()
            }

            mAdapter.setOnItemClickListener { adapter, view, position ->
                ToastUtil.showToast("$position")
            }
            // 错误刷新
            errorView.reload.setOnClickListener {
                mViewModel.load()
            }

        }
    }

    /**
     * 观察viewModel数据变化
     */
    private fun listenState() {
        with(mBinding) {
            with(mViewModel) {
                loadState.addOnPropertyChangedCallback(OnPropertyChangedCallbackImp { _, _ ->
                    when (loadState.get()) {
                        is LoadState.Loading -> {
                            refreshLayout.isRefreshing = false
                            refreshLayout.isEnabled = false
                            mAdapter.emptyView = loadingView
                            mAdapter.notifyDataSetChanged()
                        }
                        is LoadState.Failed -> {
                            mAdapter.emptyView = errorView
                            mAdapter.notifyDataSetChanged()
                            refreshLayout.isEnabled = true
                        }
                        else -> {
                            refreshLayout.isEnabled = true
                        }
                    }
                })
            }
        }
    }

    /**
     * 初始化头部
     */
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