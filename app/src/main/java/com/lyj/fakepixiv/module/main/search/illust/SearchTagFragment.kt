package com.lyj.fakepixiv.module.main.search.illust


import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.FragmentationFragment
import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.constant.IllustCategory.ILLUST
import com.lyj.fakepixiv.app.utils.bindState
import com.lyj.fakepixiv.app.utils.dp2px
import com.lyj.fakepixiv.databinding.CommonList
import com.lyj.fakepixiv.widget.CommonItemDecoration


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