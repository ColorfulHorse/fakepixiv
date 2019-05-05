package com.lyj.fakepivix.module.main.news.follow.illust

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import com.flyco.tablayout.listener.CustomTabEntity
import com.lyj.fakepivix.GlideApp
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingViewHolder
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.entity.TabBean
import com.lyj.fakepivix.app.utils.ToastUtil
import com.lyj.fakepivix.app.utils.attachLoadMore
import com.lyj.fakepivix.app.utils.dp2px
import com.lyj.fakepivix.databinding.CommonList
import com.lyj.fakepivix.databinding.FragmentNewsFollowBinding
import com.lyj.fakepivix.databinding.ItemHomeIllustBinding
import com.lyj.fakepivix.module.main.common.adapter.IllustAdapter
import com.lyj.fakepivix.widget.CommonItemDecoration
import kotlinx.android.synthetic.main.layout_common_refresh_recycler.*
import me.yokeyword.fragmentation.ISupportFragment


/**
 * @author greensun
 *
 * @date 2019/4/3
 *
 * @desc 最新-关注-插画/漫画
 */
class FollowIllustFragment : FragmentationFragment<CommonList, FollowIllustViewModel>() {

    override var mViewModel: FollowIllustViewModel = FollowIllustViewModel()

    companion object {
        fun newInstance() = FollowIllustFragment()
    }

    private lateinit var layoutManager: GridLayoutManager
    private lateinit var mAdapter: IllustAdapter


    override fun init(savedInstanceState: Bundle?) {
        initList()
    }

    /**
     * 初始化列表
     */
    private fun initList() {
        layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)

        mAdapter = IllustAdapter(mViewModel.data)
        TODO()
        //mAdapter.addItemType()

        with(mBinding) {
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
                            GlideApp.with(this@FollowIllustFragment).clear(binding.image)
                        }
                    }
                }
            }

            // 刷新
            refreshLayout.setOnRefreshListener {
                mViewModel.load()
            }

            mAdapter.setOnItemClickListener { adapter, view, position ->
                ToastUtil.showToast("$position")
            }
        }
    }

    override fun immersionBarEnabled(): Boolean = false

    override fun bindLayout(): Int = R.layout.layout_common_recycler

}