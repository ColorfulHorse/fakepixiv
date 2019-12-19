package com.lyj.fakepixiv.module.illust.series

import androidx.databinding.ViewDataBinding
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.gyf.immersionbar.ImmersionBar
import androidx.databinding.library.baseAdapters.BR
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.adapter.BaseBindingViewHolder
import com.lyj.fakepixiv.app.base.BackFragment
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.databinding.attachLoadMore
import com.lyj.fakepixiv.app.utils.bindState
import com.lyj.fakepixiv.app.utils.dp2px
import com.lyj.fakepixiv.databinding.FragmentComicSeriseBinding
import com.lyj.fakepixiv.module.common.adapter.IllustAdapter
import com.lyj.fakepixiv.widget.CommonItemDecoration

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
                    helper.binding?.setVariable(BR.index, mViewModel.detail.series_work_count - helper.adapterPosition)
                }
            }.apply {
                addItemType(Illust.TYPE_COMIC, R.layout.item_comic_series, BR.data)
            }
            val header = SeriesListHeader(mActivity, mViewModel)
            adapter.addHeaderView(header.rootView)
            adapter.bindState(mViewModel.loadState) {
                mViewModel.load()
            }
            recyclerView.attachLoadMore(mViewModel.loadMoreState) {
                mViewModel.loadMore()
            }
            val layoutManager = GridLayoutManager(mActivity, 2)
            recyclerView.layoutManager = layoutManager
            adapter.bindToRecyclerView(recyclerView)
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