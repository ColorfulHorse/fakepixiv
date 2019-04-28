package com.lyj.fakepivix.module.main.home.illust

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.view.LayoutInflater
import android.view.View
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingAdapter
import com.lyj.fakepivix.app.constant.Category
import com.lyj.fakepivix.app.constant.Constant
import com.lyj.fakepivix.app.constant.ILLUST
import com.lyj.fakepivix.app.data.model.response.SpotlightArticle
import com.lyj.fakepivix.app.databinding.OnPropertyChangedCallbackImp
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.app.utils.dp2px
import com.lyj.fakepivix.databinding.HeaderPixivisionBinding
import com.lyj.fakepivix.databinding.ItemHomeSpotlightBinding
import com.lyj.fakepivix.widget.CommonItemDecoration
import kotlinx.android.synthetic.main.layout_error_small.view.*


/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc
 */
class PixivisionHeader(val context: Context?, val viewModel: PixivisionViewModel, @Category val category: String = ILLUST) {

    private val rootView = LayoutInflater.from(context).inflate(R.layout.header_pixivision, null)

    private val loadingView: View by lazy { LayoutInflater.from(context).inflate(R.layout.layout_common_loading, null) }


    val mBinding: HeaderPixivisionBinding? = DataBindingUtil.bind(rootView)

    val adapter: BaseBindingAdapter<SpotlightArticle, ItemHomeSpotlightBinding> = BaseBindingAdapter(R.layout.item_home_spotlight, viewModel.data, BR.data)

    init {
        if (mBinding != null) {
            with(mBinding) {
                errorView.reload.setOnClickListener {
                    viewModel.load(category)
                }
                adapter.emptyView = loadingView
                val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                recyclerView.layoutManager = layoutManager
                recyclerView.addItemDecoration(CommonItemDecoration.Builder()
                        .draw(false)
                        .edge(16.dp2px(), 5.dp2px())
                        .horizontalWidth(10.dp2px())
                        .build())
                PagerSnapHelper().attachToRecyclerView(recyclerView)
                recyclerView.adapter = adapter


                with(viewModel) {
                    viewModel.loadState.addOnPropertyChangedCallback(OnPropertyChangedCallbackImp { _, _ ->
                        loadFailed = when (loadState.get()) {
                            is LoadState.Failed -> {
                                true
                            }
                            else -> {
                                false
                            }
                        }
                    })
                }
            }
        }
    }
}