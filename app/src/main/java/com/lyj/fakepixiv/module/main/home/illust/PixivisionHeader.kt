package com.lyj.fakepixiv.module.main.home.illust

import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.library.baseAdapters.BR
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.constant.IllustCategory.ILLUST
import com.lyj.fakepixiv.app.databinding.onPropertyChangedCallback

import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.app.utils.Router
import com.lyj.fakepixiv.app.utils.bindState
import com.lyj.fakepixiv.app.utils.dp2px
import com.lyj.fakepixiv.databinding.LayoutPixivisionBinding
import com.lyj.fakepixiv.module.pixivision.PixivisionAdapter
import com.lyj.fakepixiv.module.pixivision.PixivisionListFragment
import com.lyj.fakepixiv.widget.CommonItemDecoration
import kotlinx.android.synthetic.main.layout_error_small.view.*


/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc
 */
class PixivisionHeader(val context: Context?, val viewModel: PixivisionViewModel, @IllustCategory val category: String = ILLUST) {

    private val rootView = LayoutInflater.from(context).inflate(R.layout.layout_pixivision, null)

    val mBinding: LayoutPixivisionBinding? = DataBindingUtil.bind(rootView)

    val adapter: PixivisionAdapter = PixivisionAdapter(R.layout.item_home_pivision, viewModel.data, BR.data)

    init {
        if (mBinding != null) {
            with(mBinding) {
                readMore.setOnClickListener {
                    Router.getTopFragment()?.start(PixivisionListFragment.newInstance(category))
                }
                val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                recyclerView.layoutManager = layoutManager
                recyclerView.addItemDecoration(CommonItemDecoration.Builder()
                        .draw(false)
                        .edge(16.dp2px(), 5.dp2px())
                        .dividerWidth(10.dp2px(), 0)
                        .build())
                PagerSnapHelper().attachToRecyclerView(recyclerView)
                recyclerView.adapter = adapter
                adapter.bindState(viewModel.loadState, errorRes = R.layout.layout_error_small) {
                    viewModel.load()
                }
            }
        }
    }
}