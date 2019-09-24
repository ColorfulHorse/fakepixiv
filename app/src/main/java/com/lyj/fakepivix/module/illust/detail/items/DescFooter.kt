package com.lyj.fakepivix.module.illust.detail.items

import android.content.Context
import android.databinding.DataBindingUtil
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.App
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.utils.dp2px
import com.lyj.fakepivix.databinding.LayoutFooterDescBinding
import com.lyj.fakepivix.module.common.adapter.IllustTagAdapter

/**
 * @author greensun
 *
 * @date 2019/6/3
 *
 * @desc 作品介绍
 */
class DescFooter(val context: Context, val data: Illust, var mBinding: LayoutFooterDescBinding? = null): DetailItem {

    override var type: Int = DetailItem.LAYOUT_DESC

    val rootView: View by lazy { LayoutInflater.from(context).inflate(R.layout.layout_footer_desc, null) }


    init {
        if (mBinding == null) {
            mBinding = DataBindingUtil.bind(rootView)
        }

        mBinding?.let {
//            val illust = data.get()
//            data.addOnPropertyChangedCallback(onPropertyChangedCallback { _, _ ->
//                val data = data.get()
//                initData(data, it)
//            })
            initData(it)
        }
    }

    private fun initData(binding: LayoutFooterDescBinding) {
        binding.data = data
        binding.desc.text = Html.fromHtml(data.caption)
        // 转换标签+#+翻译
        val tags = data.getTranslateTags()
        val adapter = IllustTagAdapter(IllustCategory.ILLUST, R.layout.item_tag, tags)
        adapter.bindToRecyclerView(binding.recyclerView)
        binding.recyclerView.layoutManager = ChipsLayoutManager.newBuilder(App.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .build()
        binding.recyclerView.addItemDecoration(SpacingItemDecoration(1.dp2px(), 6.dp2px()))
    }
}