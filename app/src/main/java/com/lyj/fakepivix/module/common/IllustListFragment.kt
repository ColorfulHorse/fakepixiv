package com.lyj.fakepivix.module.common

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.constant.IllustCategory.*


import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.databinding.onPropertyChangedCallback
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.app.databinding.attachLoadMore
import com.lyj.fakepivix.app.utils.dp2px
import com.lyj.fakepivix.databinding.CommonRefreshList
import com.lyj.fakepivix.module.common.adapter.IllustAdapter
import com.lyj.fakepivix.widget.CommonItemDecoration
import kotlinx.android.synthetic.main.layout_common_refresh_recycler.*
import kotlinx.android.synthetic.main.layout_error.view.*


/**
 * @author greensun
 *
 * @date 2019/4/3
 *
 * @desc 公共illust listFragment
 */
class IllustListFragment : FragmentationFragment<CommonRefreshList, IllustListViewModel?>() {

    override var mViewModel: IllustListViewModel? = null
    set(value) {
        value?.category = category
        field = value
    }

    var category = ILLUST
        set(value) {
            field = value
            mViewModel?.category = field
            mViewModel?.clear()
            if (onCreated) {
                transformAdapter()
            }
        }

    companion object {
        private const val EXTRA_CATEGORY = "EXTRA_CATEGORY"
        fun newInstance(@IllustCategory category: String) = IllustListFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_CATEGORY, category)
            }
        }
    }

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var mAdapter: IllustAdapter
//    private val loadingView: View by lazy { layoutInflater.inflate(R.layout.layout_common_loading, null) }
//    private val errorView: View by lazy { layoutInflater.inflate(R.layout.layout_error, null) }

    private lateinit var loadingView: View
    private lateinit var errorView: View


    override fun init(savedInstanceState: Bundle?) {
        arguments?.let {
            category = it.getString(EXTRA_CATEGORY, ILLUST)
            mViewModel?.category = category
        }
        mViewModel?.let {
            lifecycle.addObserver(it)
        }
        initList()
        listenState()
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        mViewModel?.load()
    }

    /**
     * 初始化列表
     */
    private fun initList() {
        with(mBinding) {
            mViewModel?.let {
                vm ->
                transformAdapter()
                // 加载更多
                recyclerView.attachLoadMore { vm.loadMore() }
                refreshLayout.setOnRefreshListener {
                    vm.load()
                }

                // 错误刷新
                errorView.reload.setOnClickListener {
                    vm.load()
                }
            }
        }
    }

    private fun transformAdapter() {
        with(mBinding) {
            mViewModel?.let {
                vm ->
                mAdapter = IllustAdapter(vm.data)
                when(category) {
                    ILLUST, OTHER, COMIC -> {
                        layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
                        if (category == COMIC) {
                            mAdapter.addItemType(Illust.TYPE_COMIC, R.layout.item_home_comic, BR.data)
                        }else {
                            mAdapter.apply {
                                addItemType(Illust.TYPE_ILLUST, R.layout.item_home_illust, BR.illust)
                                addItemType(Illust.TYPE_COMIC, R.layout.item_home_illust, BR.illust)
                            }
                        }
                        recyclerView.addItemDecoration(CommonItemDecoration.Builder()
                                .dividerWidth(3.5f.dp2px(), 3.5f.dp2px())
                                .build())
                    }
                    NOVEL -> {
                        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        mAdapter.apply {
                            addItemType(Illust.TYPE_NOVEL, R.layout.item_home_novel, BR.data)
                            addItemType(Illust.TYPE_ILLUST, R.layout.item_home_novel, BR.data)
                        }
                        recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
                    }
                }
                recyclerView.layoutManager = layoutManager

                loadingView = layoutInflater.inflate(R.layout.layout_common_loading, null)
                errorView = layoutInflater.inflate(R.layout.layout_error, null)

                mAdapter.emptyView = loadingView
                mAdapter.bindToRecyclerView(recyclerView)
            }
        }
    }

    /**
     * 观察viewModel数据变化
     */
    private fun listenState() {
        mViewModel?.let {
            with(it) {
                loadState.addOnPropertyChangedCallback(onPropertyChangedCallback { _, _ ->
                    when (loadState.get()) {
                        is LoadState.Loading -> {
                            mAdapter.emptyView = loadingView
                            refreshLayout.isRefreshing = false
                            mBinding.refreshLayout.isEnabled = false
                            mAdapter.notifyDataSetChanged()
                        }
                        is LoadState.Failed -> {
                            mAdapter.emptyView = errorView
                            mBinding.refreshLayout.isEnabled = true
                            mAdapter.notifyDataSetChanged()
                        }
                        else -> {
                            mBinding.refreshLayout.isEnabled = true
                        }
                    }
                })
            }
        }
    }

    fun getRecyclerView() = mBinding.recyclerView

    override fun immersionBarEnabled(): Boolean = false

    override fun bindLayout(): Int = R.layout.layout_common_refresh_recycler

}