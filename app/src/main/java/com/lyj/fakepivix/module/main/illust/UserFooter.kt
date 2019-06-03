package com.lyj.fakepivix.module.main.illust

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingAdapter
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.model.response.Tag
import com.lyj.fakepivix.databinding.ItemIllustBinding
import com.lyj.fakepivix.databinding.ItemTagBinding
import com.lyj.fakepivix.databinding.LayoutFooterDescBinding
import com.lyj.fakepivix.databinding.LayoutFooterUserBinding
import com.lyj.fakepivix.module.main.common.adapter.IllustAdapter
import com.lyj.fakepivix.widget.FlowLayoutManager

/**
 * @author greensun
 *
 * @date 2019/6/3
 *
 * @desc 用户简介
 */
class UserFooter(val context: Context, viewModel: UserFooterViewModel) {

    val rootView: View by lazy { LayoutInflater.from(context).inflate(R.layout.layout_footer_user, null) }

    val mBinding: LayoutFooterUserBinding?

    init {
        mBinding = DataBindingUtil.bind(rootView)

        mBinding?.let {
            val adapter = BaseBindingAdapter<Illust, ItemIllustBinding>(R.layout.item_illust, viewModel.data, BR.illust)
            adapter.bindToRecyclerView(it.recyclerView)
            it.recyclerView.layoutManager = GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
        }
    }
}