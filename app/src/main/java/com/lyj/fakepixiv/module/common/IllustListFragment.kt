package com.lyj.fakepixiv.module.common

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.lyj.fakepixiv.BR
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.FragmentationFragment
import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.constant.IllustCategory.*
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.databinding.attachLoadMore


import com.lyj.fakepixiv.app.databinding.onPropertyChangedCallback
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.app.utils.bindState
import com.lyj.fakepixiv.app.utils.dp2px
import com.lyj.fakepixiv.databinding.CommonRefreshList
import com.lyj.fakepixiv.module.common.adapter.IllustAdapter
import com.lyj.fakepixiv.widget.CommonItemDecoration
import kotlinx.android.synthetic.main.layout_common_refresh_recycler.*


/**
 * @author greensun
 *
 * @date 2019/4/3
 *
 * @desc 公共illust listFragment
 */
class IllustListFragment : FragmentationFragment<CommonRefreshList, IllustListViewModel?>() {

    override var mViewModel: IllustListViewModel? = null

    var category = ILLUST
        set(value) {
            field = value
            mViewModel?.clear()
            if (onCreated) {
                initList()
            }
        }

    companion object {
        private const val EXTRA_CATEGORY = "EXTRA_CATEGORY"
        fun newInstance(@IllustCategory category: String) = IllustListFragment().apply {
            this.category = category
//            arguments = Bundle().apply {
//                putString(EXTRA_CATEGORY, category)
//            }
        }
    }

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var mAdapter: IllustAdapter


    override fun init(savedInstanceState: Bundle?) {
//        arguments?.let {
//            category = it.getString(EXTRA_CATEGORY, ILLUST)
//            mViewModel?.category = category
//        }
        initList()
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        mViewModel?.load()
    }


    private fun initList() {
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
                recyclerView.attachLoadMore(vm.loadMoreState) { vm.loadMore() }
                mAdapter.bindToRecyclerView(recyclerView)
                mAdapter.bindState(vm.loadState, refreshLayout = refreshLayout) {
                    vm.load()
                }
            }
        }
    }

    fun getRecyclerView() = mBinding.recyclerView

    override fun immersionBarEnabled(): Boolean = false

    override fun bindLayout(): Int = R.layout.layout_common_refresh_recycler

}