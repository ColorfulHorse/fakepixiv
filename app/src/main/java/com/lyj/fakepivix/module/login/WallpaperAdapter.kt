package com.lyj.fakepivix.module.login

import android.databinding.ObservableList
import android.graphics.drawable.Drawable
import android.support.v7.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.GlideApp
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingAdapter
import com.lyj.fakepivix.app.adapter.BaseBindingViewHolder
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.utils.mapUrl
import com.lyj.fakepivix.databinding.ItemWallpaperBinding

/**
 * @author greensun
 *
 * @date 2019/3/24
 *
 * @desc 登录页图片墙adapter
 */
class WallpaperAdapter(data: ObservableList<Illust>, val readyCallback: (() -> Unit)) : BaseBindingAdapter<Illust, ItemWallpaperBinding>(R.layout.item_wallpaper, data, BR.illust), ListPreloader.PreloadModelProvider<Illust> {
    var start = -1
    var end = -1
    private val map = mutableMapOf<Int, Boolean>()
    override fun convert(helper: BaseBindingViewHolder<ItemWallpaperBinding>, item: Illust) {

        helper.binding?.let {
            var request = GlideApp.with(mContext)
                    .load(item.image_urls.square_medium.mapUrl())
            //if ((start == -1) or (helper.adapterPosition <= end)) {
                    request = request.addListener(object : RequestListener<android.graphics.drawable.Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: kotlin.Any?, target: Target<android.graphics.drawable.Drawable>?, isFirstResource: kotlin.Boolean): kotlin.Boolean {
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: kotlin.Any?, target: Target<android.graphics.drawable.Drawable>?, dataSource: DataSource?, isFirstResource: kotlin.Boolean): kotlin.Boolean {
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
                //}

            request.into(helper.binding.img)
        }
    }

    override fun getPreloadItems(position: Int): MutableList<Illust> = data.subList(position, position+1)

    override fun getPreloadRequestBuilder(item: Illust): RequestBuilder<*>? {
        val url = item.image_urls.square_medium.mapUrl()
        return Glide.with(mContext).load(url)
    }
}