package com.lyj.fakepivix.module.main.common

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingViewHolder
import com.lyj.fakepivix.app.adapter.PreloadMultiBindingAdapter
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.constant.IllustCategory.*


import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.databinding.OnPropertyChangedCallbackImp
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.app.utils.ToastUtil
import com.lyj.fakepivix.app.utils.attachLoadMore
import com.lyj.fakepivix.app.utils.dp2px
import com.lyj.fakepivix.databinding.CommonList
import com.lyj.fakepivix.module.main.common.adapter.ComicAdapter
import com.lyj.fakepivix.module.main.common.adapter.IllustAdapter
import com.lyj.fakepivix.module.main.common.adapter.NovelAdapter
import com.lyj.fakepivix.widget.CommonItemDecoration


/**
 * @author greensun
 *
 * @date 2019/4/3
 *
 * @desc 公共listFragment
 */
class IllustListFragment : FragmentationFragment<CommonRefreshList, IllustListViewModel?>() {

    override var mViewModel: IllustListViewModel? = null
    set(value) {
        value?.category = category
        field = value
    }

    private var category = ILLUST

    companion object {
        private const val EXTRA_CATEGORY = "EXTRA_CATEGORY"
        fun newInstance(@IllustCategory category: String) = IllustListFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_CATEGORY, category)
            }
        }
    }

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var mAdapter: PreloadMultiBindingAdapter<Illust>
    private val loadingView: View by lazy { layoutInflater.inflate(R.layout.layout_common_loading, null) }
    private val errorView: View by lazy { layoutInflater.inflate(R.layout.layout_error, null) }


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
                when(category) {
                    ILLUST, ILLUSTANDCOMIC, COMIC -> {
                        layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
                        if (category == COMIC) {
                            mAdapter = ComicAdapter(vm.data)
                        }else {
                            mAdapter = IllustAdapter(vm.data)
                            mAdapter.addItemType(Illust.TYPE_COMIC, R.layout.item_home_illust, BR.illust)
                        }
                        recyclerView.addItemDecoration(CommonItemDecoration.Builder()
                                .draw(false)
                                .verticalWidth(3.5f.dp2px())
                                .build())
                    }
                    NOVEL -> {
                        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        mAdapter = NovelAdapter(vm.data)
                        recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
                    }
                }
                mAdapter.emptyView = loadingView
                recyclerView.layoutManager = layoutManager
                mAdapter.bindToRecyclerView(recyclerView)
                // 加载更多
                recyclerView.attachLoadMore { vm.loadMore() }
                /*recyclerView.setRecyclerListener {
                    if (it is BaseBindingViewHolder<*>) {
                        it.binding?.let { binding ->
                            if (binding is ItemHomeIllustBinding) {
                                GlideApp.with(this@IllustListFragment).clear(binding.image)
                            }
                        }
                    }
                }*/
                recyclerView.setRecyclerListener {
                    if (it is BaseBindingViewHolder<*>) {

                    }
                }

                mAdapter.setOnItemClickListener { adapter, view, position ->
                    ToastUtil.showToast("$position")
                }
            }
        }
    }

    /**
     * 观察viewModel数据变化
     */
    private fun listenState() {
        mViewModel?.let {
            with(it) {
                loadState.addOnPropertyChangedCallback(OnPropertyChangedCallbackImp { _, _ ->
                    when (loadState.get()) {
                        is LoadState.Loading -> {
                            mAdapter.emptyView = loadingView
                            mAdapter.notifyDataSetChanged()
                        }
                        is LoadState.Failed -> {
                            mAdapter.emptyView = errorView
                            mAdapter.notifyDataSetChanged()
                        }
                        else -> {

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