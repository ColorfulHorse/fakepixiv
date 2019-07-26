package com.lyj.fakepivix.module.main.search.illust


import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.constant.IllustCategory.ILLUST
import com.lyj.fakepivix.app.databinding.onPropertyChangedCallback
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.app.utils.ToastUtil
import com.lyj.fakepivix.app.utils.dp2px
import com.lyj.fakepivix.databinding.CommonList
import com.lyj.fakepivix.widget.CommonItemDecoration


/**
 * @author greensun
 *
 * @date 2019/4/3
 *
 * @desc 搜索tag页面
 */
class SearchIllustFragment : FragmentationFragment<CommonList, SearchIllustViewModel>() {

    override var mViewModel: SearchIllustViewModel = SearchIllustViewModel()

    var category = ILLUST
    private set(value) {
        field = value
    }

    companion object {
        private const val EXTRA_CATEGORY = "EXTRA_CATEGORY"
        fun newInstance(@IllustCategory category: String) = SearchIllustFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_CATEGORY, category)
            }
        }
    }

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var mAdapter: SearchTagAdapter
    private val loadingView: View by lazy { layoutInflater.inflate(R.layout.layout_common_loading, null) }
    private val errorView: View by lazy { layoutInflater.inflate(R.layout.layout_error, null) }


    override fun init(savedInstanceState: Bundle?) {
        arguments?.let {
            category = it.getString(EXTRA_CATEGORY, ILLUST)
            mViewModel.category = category
        }
        initList()
        listenState()
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        mViewModel.load()
    }

    /**
     * 初始化列表
     */
    private fun initList() {
        with(mBinding) {
            with(mViewModel) {
                layoutManager = GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
                mAdapter = SearchTagAdapter(data)
                recyclerView.addItemDecoration(CommonItemDecoration.Builder()
                        .draw(false)
                        .dividerWidth(1f.dp2px(), 1f.dp2px())
                        .build())
                mAdapter.emptyView = loadingView
                recyclerView.layoutManager = layoutManager
                mAdapter.bindToRecyclerView(recyclerView)
                // 加载更多
                //recyclerView.attachLoadMore { loadMore() }

                mAdapter.setOnItemClickListener { _, _, position ->
                    ToastUtil.showToast("$position")
                }
            }
        }
    }

    /**
     * 观察viewModel数据变化
     */
    private fun listenState() {
        with(mViewModel) {
            loadState.addOnPropertyChangedCallback(onPropertyChangedCallback { _, _ ->
                when (loadState.get()) {
                    is LoadState.Loading -> {
                        mAdapter.emptyView = loadingView
                        //mBinding.refreshLayout.isEnabled = false
                        mAdapter.notifyDataSetChanged()
                    }
                    is LoadState.Failed -> {
                        mAdapter.emptyView = errorView
                        //mBinding.refreshLayout.isEnabled = true
                        mAdapter.notifyDataSetChanged()
                    }
                    else -> {
                        //mBinding.refreshLayout.isEnabled = true
                    }
                }
            })
        }
    }

    override fun immersionBarEnabled(): Boolean = false

    override fun bindLayout(): Int = R.layout.layout_common_recycler

}