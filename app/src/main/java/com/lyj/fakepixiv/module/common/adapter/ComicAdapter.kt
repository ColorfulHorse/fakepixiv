package com.lyj.fakepixiv.module.common.adapter

import androidx.databinding.ObservableList
import androidx.databinding.ViewDataBinding
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import com.bumptech.glide.RequestBuilder
import androidx.databinding.library.baseAdapters.BR
import com.lyj.fakepixiv.GlideApp
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.adapter.BaseBindingViewHolder
import com.lyj.fakepixiv.app.adapter.PreloadMultiBindingAdapter
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.utils.Router
import com.lyj.fakepixiv.databinding.ItemHomeComicBinding

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc
 */
@Deprecated("合并为IllustAdapter")
open class ComicAdapter(data: ObservableList<Illust>) : PreloadMultiBindingAdapter<Illust>(data) {

    init {
        addItemType(Illust.TYPE_COMIC, R.layout.item_home_comic, BR.data)
        setOnItemClickListener { _, _, position ->
            Router.goDetail(position, data)
        }
    }
}