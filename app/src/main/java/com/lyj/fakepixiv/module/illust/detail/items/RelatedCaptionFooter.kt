package com.lyj.fakepixiv.module.illust.detail.items

import android.content.Context
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.databinding.FooterRelatedCaptionBinding
import kotlinx.android.synthetic.main.layout_error_small.view.*

/**
 * @author greensun
 *
 * @date 2019/6/3
 *
 * @desc 用户简介
 */
class RelatedCaptionFooter(val context: Context, val viewModel: RelatedCaptionViewModel): DetailItem {

    override var type: Int = DetailItem.LAYOUT_RELATED_CAPTION

    val rootView: View by lazy { LayoutInflater.from(context).inflate(R.layout.footer_related_caption, null) }

    val mBinding: FooterRelatedCaptionBinding?


    init {
        mBinding = DataBindingUtil.bind(rootView)

        mBinding?.let {
            it.vm = viewModel
            it.errorView.reload.setOnClickListener {
                viewModel.parent.load()
            }
        }
    }
}