package com.lyj.fakepivix.module.illust.series

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import com.gyf.barlibrary.ImmersionBar
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingAdapter
import com.lyj.fakepivix.app.adapter.BaseBindingViewHolder
import com.lyj.fakepivix.app.base.BackFragment
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.source.remote.UserRepository
import com.lyj.fakepivix.app.databinding.attachLoadMore
import com.lyj.fakepivix.app.utils.bindState
import com.lyj.fakepivix.app.utils.dp2px
import com.lyj.fakepivix.databinding.FragmentComicSeriseBinding
import com.lyj.fakepivix.databinding.FragmentUserDetailBinding
import com.lyj.fakepivix.databinding.ItemWorkspaceBinding
import com.lyj.fakepivix.databinding.ItemWorkspaceBindingImpl
import com.lyj.fakepivix.module.common.adapter.IllustAdapter
import com.lyj.fakepivix.module.user.detail.UserDetailViewModel
import com.lyj.fakepivix.widget.CommonItemDecoration

/**
 * @author greensun
 *
 * @date 2019/3/20
 *
 * @desc 漫画系列
 */
class ComicSeriesFragment : BackFragment<FragmentComicSeriseBinding, ComicSeriesViewModel>() {

    override var mViewModel: ComicSeriesViewModel = ComicSeriesViewModel()

    private var seriesId: String = ""

    companion object {
        private const val EXTRA_SERIES_ID = "EXTRA_SERIES_ID"
        fun newInstance(seriesId: String): ComicSeriesFragment {
            return ComicSeriesFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_SERIES_ID, seriesId)
                }
            }
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        arguments?.let {
            seriesId = it.getString(EXTRA_SERIES_ID, "")
            mViewModel.seriesId = seriesId
        }
        mToolbar?.inflateMenu(R.menu.menu_comic_series)

        with(mBinding) {
            val adapter = object : IllustAdapter(mViewModel.data, true) {
                override fun convert(helper: BaseBindingViewHolder<ViewDataBinding>, item: Illust) {
                    super.convert(helper, item)
                    helper.binding?.setVariable(BR.index, mViewModel.detail.series_work_count)
                }
            }.apply {
                addItemType(Illust.TYPE_COMIC, R.layout.item_comic_series, BR.data)
            }
            val header = SeriesListHeader(mActivity, mViewModel)
            adapter.addHeaderView(header.rootView)
            adapter.bindState(mViewModel.loadState) {
                mViewModel.load()
            }
            recyclerView.attachLoadMore {
                mViewModel.loadMore()
            }
            val layoutManager = GridLayoutManager(mActivity, 2)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
            recyclerView.addItemDecoration(CommonItemDecoration.Builder()
                    .dividerWidth(10.dp2px(), 10.dp2px())
                    .edge(0, 16.dp2px())
                    .build())
        }
        mViewModel.load()
    }



    override fun initImmersionBar() {
        ImmersionBar.with(this)
                .titleBarMarginTop(mBinding.toolbar)
                .statusBarColor(R.color.transparent)
                .statusBarColorTransform(R.color.black)
                .statusBarAlpha(0.25f)
                .init()
    }


    override fun bindLayout(): Int = R.layout.fragment_comic_serise
}