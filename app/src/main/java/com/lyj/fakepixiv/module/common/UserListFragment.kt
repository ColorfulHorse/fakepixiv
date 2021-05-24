package com.lyj.fakepixiv.module.common


import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.FragmentationFragment
import com.lyj.fakepixiv.app.utils.attachLoadMore
import com.lyj.fakepixiv.app.utils.bindState
import com.lyj.fakepixiv.app.utils.dp2px
import com.lyj.fakepixiv.databinding.CommonRefreshList
import com.lyj.fakepixiv.module.common.adapter.UserPreviewAdapter
import com.lyj.fakepixiv.widget.CommonItemDecoration


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

                mAdapter.bindState(vm.loadState,  refreshLayout = refreshLayout) {
                    vm.load()
                }
            }
        }
    }

    override fun immersionBarEnabled(): Boolean = false

    override fun bindLayout(): Int = R.layout.layout_common_refresh_recycler

}