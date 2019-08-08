package com.lyj.fakepivix.module.main.illust

import android.content.Context
import android.databinding.DataBindingUtil
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingAdapter
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.model.response.Tag
import com.lyj.fakepivix.databinding.ItemTagBinding
import com.lyj.fakepivix.databinding.LayoutFooterDescBinding
import com.lyj.fakepivix.widget.FlowLayoutManager

/**
 * @author greensun
 *
 * @date 2019/6/3
 *
 * @desc 作品介绍
 */
class DescFooter(val context: Context, val data: Illust, var mBinding: LayoutFooterDescBinding? = null) {

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
        val tags = data.tags.flatMap { tag ->
            val list = mutableListOf<Tag>()
            if (!TextUtils.isEmpty(tag.name)) {
                list.add(tag.copy(name = "#${tag.name}"))
                if (!TextUtils.isEmpty(tag.translated_name)) {
                    list.add(tag.copy(isTranslated = true))
                }
            }
            list
        }.toMutableList()
        val adapter = BaseBindingAdapter<Tag, ItemTagBinding>(R.layout.item_tag, tags, BR.data)
        adapter.bindToRecyclerView(binding.recyclerView)
        binding.recyclerView.layoutManager = FlowLayoutManager()
    }
}