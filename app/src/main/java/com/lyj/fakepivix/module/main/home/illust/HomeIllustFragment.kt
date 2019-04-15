package com.lyj.fakepivix.module.main.home.illust

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import com.flyco.tablayout.listener.CustomTabEntity
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseMultiBindingAdapter
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.FragmentationFragment;
import com.lyj.fakepivix.app.entity.TabBean
import com.lyj.fakepivix.databinding.*


/**
 * @author greensun
 *
 * @date 2019/4/3
 *
 * @desc
 */
class HomeIllustFragment : FragmentationFragment<CommonRefreshList, HomeIllustViewModel>() {

    override var mViewModel = HomeIllustViewModel()

    companion object {
        fun newInstance() = HomeIllustFragment()
    }

    private lateinit var layoutManager: GridLayoutManager
    private lateinit var mAdapter: HomeIllustAdapter
    // 排行榜，直播，pixivision头部
    private lateinit var rankHeader: RankHeader
    private lateinit var liveHeader: LiveHeader
    private lateinit var pixivisionHeader: PixivisionHeader

    override fun init(savedInstanceState: Bundle?) {
        layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
        mAdapter = HomeIllustAdapter(mViewModel.data)
        with(mBinding) {
            initHeader()
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = mAdapter
        }
    }

    private fun initHeader() {
        val title = layoutInflater.inflate(R.layout.header_recommend, null)
        lifecycle.addObserver(mViewModel.liveViewModel)
        lifecycle.addObserver(mViewModel.pixivisionViewModel)
        rankHeader = RankHeader(context, mViewModel.rankViewModel)
        liveHeader = LiveHeader(context, mViewModel.liveViewModel)
        pixivisionHeader = PixivisionHeader(context, mViewModel.pixivisionViewModel)
        mAdapter.addHeaderView(title)
        mAdapter.addHeaderView(liveHeader.mBinding?.root)
        mAdapter.addHeaderView(rankHeader.mBinding?.root)
        mAdapter.addHeaderView(pixivisionHeader.mBinding?.root, 10)
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        mViewModel.lazyLoad()
    }

    override fun immersionBarEnabled(): Boolean = false

    override fun bindLayout(): Int = R.layout.layout_common_recycler

}