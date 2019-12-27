package com.lyj.fakepixiv.module.main.home.novel

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.ImageView
import androidx.recyclerview.widget.DividerItemDecoration
import com.chad.library.adapter.base.BaseViewHolder
import androidx.databinding.library.baseAdapters.BR
import com.lyj.fakepixiv.GlideApp
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.FragmentationFragment
import com.lyj.fakepixiv.app.constant.IllustCategory.NOVEL
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.utils.attachLoadMore
import com.lyj.fakepixiv.app.utils.bindState
import com.lyj.fakepixiv.databinding.CommonRefreshList
import com.lyj.fakepixiv.module.common.adapter.IllustAdapter
import com.lyj.fakepixiv.module.main.home.illust.RankHeader


/**
 * @author greensun
 *
 * @date 2019/4/3
 *
 * @desc
 */
class HomeNovelFragment : FragmentationFragment<CommonRefreshList, HomeNovelViewModel>() {

    override var mViewModel: HomeNovelViewModel = HomeNovelViewModel()

    companion object {
        fun newInstance() = HomeNovelFragment()
    }


    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var mAdapter: IllustAdapter
    // 排行榜
    private lateinit var rankHeader: RankHeader

    override fun init(savedInstanceState: Bundle?) {
        initList()
    }

    /**
     * 初始化列表
     */
    private fun initList() {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mAdapter = IllustAdapter(mViewModel.data).apply {
            addItemType(Illust.TYPE_NOVEL, R.layout.item_home_novel, BR.data)
            addItemType(Illust.TYPE_ILLUST, R.layout.item_home_novel, BR.data)
        }

        with(mBinding) {
            initHeader()
            recyclerView.layoutManager = layoutManager
            mAdapter.bindToRecyclerView(recyclerView)
            recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            // 加载更多
            recyclerView.attachLoadMore(mViewModel.loadMoreState) { mViewModel.loadMore() }

            recyclerView.setRecyclerListener {
                if (it is BaseViewHolder) {
                    val image = it.getView<ImageView>(R.id.image)
                    image?.let {
                        GlideApp.with(this@HomeNovelFragment).clear(image)
                    }
                }
            }

            mAdapter.bindState(mViewModel.loadState, refreshLayout = refreshLayout) {
                mViewModel.load()
            }

        }
    }

    /**
     * 初始化头部
     */
    private fun initHeader() {
        val title = layoutInflater.inflate(R.layout.header_recommend, null)
        rankHeader = RankHeader(context, mViewModel.rankViewModel, NOVEL)
        mAdapter.addHeaderView(rankHeader.mBinding?.root)
        mAdapter.addHeaderView(title)
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        mViewModel.lazyLoad()
    }


    override fun immersionBarEnabled(): Boolean = false

    override fun bindLayout(): Int = R.layout.layout_common_refresh_recycler

}