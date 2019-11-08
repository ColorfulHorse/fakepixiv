package com.lyj.fakepixiv.module.illust.detail.items

import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import com.lyj.fakepixiv.BR
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.databinding.onPropertyChangedCallback
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.app.utils.Router
import com.lyj.fakepixiv.app.utils.bindState
import com.lyj.fakepixiv.app.utils.dp2px
import com.lyj.fakepixiv.databinding.LayoutFooterUserBinding
import com.lyj.fakepixiv.module.common.adapter.IllustAdapter
import com.lyj.fakepixiv.widget.CommonItemDecoration
import kotlinx.android.synthetic.main.layout_error_small.view.*

/**
 * @author greensun
 *
 * @date 2019/6/3
 *
 * @desc 用户简介
 */
class UserFooter(val context: Context, val viewModel: UserFooterViewModel, var mBinding: LayoutFooterUserBinding? = null): DetailItem {

    override var type: Int = DetailItem.LAYOUT_USER

    val rootView: View by lazy { LayoutInflater.from(context).inflate(R.layout.layout_footer_user, null) }

    var mAdapter = IllustAdapter(viewModel.data).apply {
        addItemType(Illust.TYPE_ILLUST, R.layout.item_illust_small, BR.illust)
        addItemType(Illust.TYPE_COMIC, R.layout.item_illust_small, BR.illust)
        addItemType(Illust.TYPE_NOVEL, R.layout.item_novel_small, BR.illust)
    }

    init {
        if (mBinding == null) {
            mBinding = DataBindingUtil.bind(rootView)
        }
        mBinding?.let {
            it.vm = viewModel
            it.recyclerView.layoutManager = GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
            it.recyclerView.addItemDecoration(CommonItemDecoration.Builder().dividerWidth(1.dp2px(), 0).draw(false).build())
            mAdapter.bindToRecyclerView(it.recyclerView)
            it.avatar.setOnClickListener {
                Router.goUserDetail(viewModel.parent.illust.user)
            }
            it.nickName.setOnClickListener {
                Router.goUserDetail(viewModel.parent.illust.user)
            }
        }
        mAdapter.bindState(viewModel.loadState, loadingRes = R.layout.layout_common_loading_white, errorRes = R.layout.layout_error_small) {
            viewModel.load()
        }
    }
}