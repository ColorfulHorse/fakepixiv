package com.lyj.fakepivix.app.databinding

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * @author greensun
 *
 * @date 2019/3/21
 *
 * @desc
 */
class ImageViewBindingAdapter {
    companion object {
        @BindingAdapter("bind:url")
        fun showPic (imageView: ImageView, url: String) {

        }
    }
}