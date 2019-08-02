package com.lyj.fakepivix.module.common.adapter;

import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import com.bumptech.glide.RequestBuilder
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.GlideApp
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.PreloadBindingAdapter
import com.lyj.fakepivix.app.data.model.response.UserPreview

/**
 * @author greensun
 * @date 2019/8/2
 * @desc
 */
class UserPreviewAdapter(data: ObservableList<UserPreview>) : PreloadBindingAdapter<UserPreview, ViewDataBinding>(R.layout.item_user_preview, data, BR.data) {



    override fun getPreloadRequestBuilder(item: UserPreview): RequestBuilder<*>? {
        var req = GlideApp.with(mContext)
                .load(item.user.profile_image_urls.medium)
        for (illust in item.illusts) {
            req = req.load(illust.image_urls.medium)
        }
        return req
    }

}
