package com.lyj.fakepivix.module.login

import android.databinding.ObservableList
import android.graphics.drawable.Drawable
import android.support.annotation.LayoutRes
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import com.bumptech.glide.*
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.GlideApp
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingAdapter
import com.lyj.fakepivix.app.databinding.url
import com.lyj.fakepivix.app.model.response.Illust
import com.lyj.fakepivix.app.utils.mapUrl
import com.lyj.fakepivix.databinding.ItemWallpaperBinding

/**
 * @author greensun
 *
 * @date 2019/3/24
 *
 * @desc
 */
class WallpaperAdapter(data: ObservableList<Illust>, val readyCallback: (() -> Unit)) : BaseBindingAdapter<Illust, ItemWallpaperBinding>(R.layout.item_wallpaper, data, BR.illust), ListPreloader.PreloadModelProvider<Illust> {
    var start = -1
    var end = -1
    private val map = mutableMapOf<Int, Boolean>()
    override fun convert(helper: BaseBindingAdapter.BaseBindingViewHolder<ItemWallpaperBinding>, item: Illust) {

        helper.binding?.let {
            GlideApp.with(mContext)
                    .load(item.image_urls.square_medium.mapUrl())
                    .addListener(object : RequestListener<Drawable>{
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            val layoutManager = recyclerView.layoutManager as GridLayoutManager
                            if (start == -1) {
                                // 计算可见item数
                                start = layoutManager.findFirstVisibleItemPosition()
                                end = layoutManager.findLastVisibleItemPosition()
                                for (i in start..end) {
                                    map[i] = false
                                }
                            }
                            map[helper.adapterPosition] = true
                            // 可见item图片全部加载完成
                            if (map.values.reduce { a, b -> a and b }) {
                                readyCallback()
                            }
                            return false
                        }

                    })
                    .into(helper.binding.img)
        }
    }

    override fun getPreloadItems(position: Int): MutableList<Illust> = data.subList(position, position+1)

    override fun getPreloadRequestBuilder(item: Illust): RequestBuilder<*>? {
        return Glide.with(mContext).load(item)
    }
}