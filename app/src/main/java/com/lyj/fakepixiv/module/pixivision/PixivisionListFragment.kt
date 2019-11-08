package com.lyj.fakepixiv.module.pixivision

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gyf.barlibrary.ImmersionBar
import com.lyj.fakepixiv.BR
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.BackFragment
import com.lyj.fakepixiv.app.constant.EXTRA_CATEGORY
import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.databinding.attachLoadMore
import com.lyj.fakepixiv.app.utils.bindState
import com.lyj.fakepixiv.app.utils.dp2px
import com.lyj.fakepixiv.databinding.FragmentCommonListBinding
import com.lyj.fakepixiv.module.main.home.illust.PixivisionViewModel
import com.lyj.fakepixiv.widget.CommonItemDecoration

/**
 * @author green sun
 *
 * @date 2019/10/8
 *
 * @desc 特辑webView
 */
class PixivisionListFragment : BackFragment<FragmentCommonListBinding, PixivisionViewModel>() {

    override var mViewModel: PixivisionViewModel = PixivisionViewModel()

    var category = IllustCategory.ILLUST

    companion object {

        fun newInstance(@IllustCategory category: String) = PixivisionListFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_CATEGORY, category)
            }
        }
    }

    override fun init(savedInstanceState: Bundle?) {

        arguments?.let {
            category = it.getString(EXTRA_CATEGORY, IllustCategory.ILLUST)
            mViewModel.category = category
        }
        mToolbar?.title = getString(R.string.title_pixivision)
        with(mBinding) {
            val adapter = PixivisionAdapter(R.layout.item_pixivision, mViewModel.data, BR.data)
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerView.layoutManager = layoutManager
            recyclerView.addItemDecoration(CommonItemDecoration.Builder()
                    .draw(false)
                    .edge(8.dp2px(), 8.dp2px())
                    .dividerWidth(10.dp2px(), 0)
                    .build())
            val header = layoutInflater.inflate(R.layout.header_pixivision, null)
            adapter.addHeaderView(header)
            adapter.bindToRecyclerView(recyclerView)
            adapter.bindState(mViewModel.loadState, refreshLayout = refreshLayout) {
                mViewModel.load()
            }
            recyclerView.attachLoadMore(mViewModel.loadMoreState) { mViewModel.loadMore() }
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        mViewModel.load()
    }


    override fun initImmersionBar() {
        ImmersionBar.with(this)
                .titleBarMarginTop(mBinding.contentView)
                .statusBarColor(R.color.transparent)
                .statusBarColorTransform(R.color.black)
                .statusBarAlpha(0.25f)
                .init()
    }

    override fun bindBackIcon(): Drawable {
        return createDefaultBack()
    }

    override fun bindLayout(): Int = R.layout.fragment_common_list
}