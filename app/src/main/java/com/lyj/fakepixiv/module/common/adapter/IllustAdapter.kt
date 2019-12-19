package com.lyj.fakepixiv.module.common.adapter

import androidx.databinding.ViewDataBinding
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.RequestBuilder
import com.lyj.fakepixiv.GlideApp
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.adapter.BaseBindingViewHolder
import com.lyj.fakepixiv.app.adapter.PreloadMultiBindingAdapter
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.widget.bindAction
import com.lyj.fakepixiv.app.utils.Router
import com.lyj.fakepixiv.module.common.StarDialog
import com.lyj.fakepixiv.widget.LikeButton

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc
 */
open class IllustAdapter(data: MutableList<Illust>, val likeButton: Boolean = true) : PreloadMultiBindingAdapter<Illust>(data) {

    init {
        setOnItemClickListener { _, _, position ->
            Router.goDetail(getRealPosition(position), mData)
        }
    }

    override fun convert(helper: BaseBindingViewHolder<ViewDataBinding>, item: Illust) {
        super.convert(helper, item)
        val button = helper.getView<LikeButton>(R.id.like)
        val series = helper.getView<View>(R.id.series_title)
        button?.let {
            if (!likeButton) it.visibility = View.GONE
            it.bindAction(item)
            it.setOnLongClickListener {
                Router.getActiveFragment()?.let { f ->
                    val dialog = StarDialog.newInstance()
                    dialog.mViewModel.data = item
                    dialog.show(f.childFragmentManager, "StarDialog")
                }
                true
            }
        }
        series?.let {
            series.setOnClickListener {
                // 系列
                Router.goNovelSeries(item.series?.id.toString())
            }
        }
    }


    override fun isFixedViewType(type: Int): Boolean {
        return super.isFixedViewType(type) or (type == Illust.TYPE_META)
    }
}