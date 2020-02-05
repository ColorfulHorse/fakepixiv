package com.lyj.fakepixiv.module.illust.detail.items

import android.content.Context
import androidx.databinding.DataBindingUtil
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.application.ApplicationLike
import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.utils.Router
import com.lyj.fakepixiv.app.utils.dp2px
import com.lyj.fakepixiv.databinding.LayoutDetailDescBinding
import com.lyj.fakepixiv.module.common.adapter.IllustTagAdapter

/**
 * @author greensun
 *
 * @date 2019/6/3
 *
 * @desc 作品介绍
 */
class DescFooter(val context: Context, val data: Illust, var mBinding: LayoutDetailDescBinding? = null): DetailItem {

    override var type: Int = DetailItem.LAYOUT_DESC

    val rootView: View by lazy { LayoutInflater.from(context).inflate(R.layout.layout_detail_desc, null) }


    init {
        if (mBinding == null) {
            mBinding = DataBindingUtil.bind(rootView)
            mBinding?.data = data
        }

        mBinding?.let {
            initData(it)
        }
    }

    private fun initData(binding: LayoutDetailDescBinding) {
        binding.desc.text = Html.fromHtml(data.caption)
        // 转换标签+#+翻译
        val tags = data.getTranslateTags()
        val adapter = IllustTagAdapter(IllustCategory.ILLUST, R.layout.item_tag, tags)
        adapter.bindToRecyclerView(binding.recyclerView)
        binding.recyclerView.layoutManager = ChipsLayoutManager.newBuilder(ApplicationLike.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .build()
        binding.recyclerView.addItemDecoration(SpacingItemDecoration(1.dp2px(), 6.dp2px()))
        binding.avatar.setOnClickListener {
            Router.goUserDetail(data.user)
        }
        binding.nickName.setOnClickListener {
            Router.goUserDetail(data.user)
        }
    }
}