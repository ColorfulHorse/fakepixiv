package com.lyj.fakepivix.module.main.search.illust


import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.constant.IllustCategory.ILLUST
import com.lyj.fakepivix.app.utils.ToastUtil
import com.lyj.fakepivix.app.utils.bindState
import com.lyj.fakepivix.app.utils.dp2px
import com.lyj.fakepivix.databinding.CommonList
import com.lyj.fakepivix.widget.CommonItemDecoration


/**
 * @author greensun
 *
 * @date 2019/4/3
 *
 * @desc 搜索 - 关键字列表页
 */
class SearchTagFragment : FragmentationFragment<CommonList, SearchTagViewModel>() {

    override var mViewModel: SearchTagViewModel = SearchTagViewModel()

    var category = ILLUST
    private set(value) {
        field = value
    }

    companion object {
        private const val EXTRA_CATEGORY = "EXTRA_CATEGORY"
        fun newInstance(@IllustCategory category: String) = SearchTagFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_CATEGORY, category)
            }
        }
    }

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var mAdapter: SearchTagAdapter


    override fun init(savedInstanceState: Bundle?) {
        arguments?.let {
            category = it.getString(EXTRA_CATEGORY, ILLUST)
            mViewModel.category = category
        }
        initList()
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
                recyclerView.layoutManager = layoutManager
                mAdapter.bindToRecyclerView(recyclerView)
                // 加载更多
                //recyclerView.attachLoadMore { loadMore() }
                mAdapter.bindState(loadState) {
                    load()
                }
            }
        }
    }


    override fun immersionBarEnabled(): Boolean = false

    override fun bindLayout(): Int = R.layout.layout_common_recycler

}