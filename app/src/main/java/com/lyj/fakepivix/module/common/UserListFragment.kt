package com.lyj.fakepivix.module.common


import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.databinding.attachLoadMore
import com.lyj.fakepivix.app.utils.bindState
import com.lyj.fakepivix.app.utils.dp2px
import com.lyj.fakepivix.databinding.CommonRefreshList
import com.lyj.fakepivix.module.common.adapter.IllustAdapter
import com.lyj.fakepivix.module.common.adapter.UserPreviewAdapter
import com.lyj.fakepivix.widget.CommonItemDecoration


/**
 * @author greensun
 *
 * @date 2019/4/3
 *
 * @desc 公共user列表
 */
class UserListFragment : FragmentationFragment<CommonRefreshList, UserListViewModel?>() {

    override var mViewModel: UserListViewModel? = null

    companion object {
        fun newInstance() = UserListFragment()
    }

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var mAdapter: UserPreviewAdapter

    override fun init(savedInstanceState: Bundle?) {
        initList()
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
                mAdapter = UserPreviewAdapter(vm.data)
                layoutManager = LinearLayoutManager(context)
                recyclerView.layoutManager = layoutManager
                mAdapter.bindToRecyclerView(recyclerView)
                recyclerView.addItemDecoration(CommonItemDecoration.Builder()
                        .dividerWidth(16.dp2px(), 0)
                        .draw(true)
                        .color(ContextCompat.getColor(mActivity, R.color.bg_general))
                        .build())
                // 加载更多
                recyclerView.attachLoadMore(vm.loadMoreState) { vm.loadMore() }
//                refreshLayout.setOnRefreshListener {
//                    vm.load()
//                }

                mAdapter.bindState(vm.loadState, reload = vm::load , refreshLayout = refreshLayout)
            }
        }
    }

    fun getRecyclerView() = mBinding.recyclerView

    override fun immersionBarEnabled(): Boolean = false

    override fun bindLayout(): Int = R.layout.layout_common_refresh_recycler

}