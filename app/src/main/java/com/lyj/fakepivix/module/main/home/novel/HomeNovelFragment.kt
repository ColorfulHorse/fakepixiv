package com.lyj.fakepivix.module.main.home.novel

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.lyj.fakepivix.GlideApp
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingViewHolder
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.constant.NOVEL
import com.lyj.fakepivix.app.databinding.OnPropertyChangedCallbackImp
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.app.utils.ToastUtil
import com.lyj.fakepivix.app.utils.attachLoadMore
import com.lyj.fakepivix.databinding.CommonRefreshList
import com.lyj.fakepivix.databinding.ItemHomeIllustBinding
import com.lyj.fakepivix.module.main.common.adapter.NovelAdapter
import com.lyj.fakepivix.module.main.home.illust.RankHeader
import kotlinx.android.synthetic.main.layout_error.view.*


/**
 * @author greensun
 *
 * @date 2019/4/3
 *
 * @desc
 */
class HomeNovelFragment : FragmentationFragment<CommonRefreshList, HomeNovelViewModel>() {

    override var mViewModel: HomeNovelViewModel = HomeNovelViewModel()

    companion object {
        fun newInstance() = HomeNovelFragment()
    }


    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var mAdapter: NovelAdapter
    // 排行榜
    private lateinit var rankHeader: RankHeader

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
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mAdapter = NovelAdapter(mViewModel.data)

        with(mBinding) {
            initHeader()
            recyclerView.layoutManager = layoutManager
            mAdapter.bindToRecyclerView(recyclerView)
            recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            // 加载更多
            recyclerView.attachLoadMore { mViewModel.loadMore() }

            recyclerView.setRecyclerListener {
                if (it is BaseBindingViewHolder<*>) {
                    it.binding?.let { binding ->
                        if (binding is ItemHomeIllustBinding) {
                            GlideApp.with(this@HomeNovelFragment).clear(binding.image)
                        }
                    }
                }
            }

            // 刷新
            refreshLayout.setOnRefreshListener {
                mViewModel.lazyLoad()
            }

            mAdapter.setOnItemClickListener { _, _, position ->
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
        rankHeader = RankHeader(context, mViewModel.rankViewModel, NOVEL)
        mAdapter.addHeaderView(rankHeader.mBinding?.root)
        mAdapter.addHeaderView(title)
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        mViewModel.lazyLoad()
    }


    override fun immersionBarEnabled(): Boolean = false

    override fun bindLayout(): Int = R.layout.layout_common_recycler

}