package com.lyj.fakepivix.module.main.illust

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
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
class DescFooter(val context: Context, data: Illust) {

    val rootView: View by lazy { LayoutInflater.from(context).inflate(R.layout.layout_footer_desc, null) }

    val mBinding: LayoutFooterDescBinding?

    init {
        mBinding = DataBindingUtil.bind(rootView)

        mBinding?.let {
            it.data = data
            it.desc.text = Html.fromHtml(data.caption)
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
            }
            val adapter = BaseBindingAdapter<Tag, ItemTagBinding>(R.layout.item_tag, tags, BR.data)
            adapter.bindToRecyclerView(it.recyclerView)
            it.recyclerView.layoutManager = FlowLayoutManager()
        }
    }
}