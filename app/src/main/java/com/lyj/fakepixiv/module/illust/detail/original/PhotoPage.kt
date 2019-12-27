package com.lyj.fakepixiv.module.illust.detail.original

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.lyj.fakepixiv.GlideApp
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.network.ApiException
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.databinding.LayoutPhotoViewBinding
import kotlinx.android.synthetic.main.layout_error.view.*

/**
 * @author green sun
 *
 * @date 2019/11/11
 *
 * @desc
 */
class PhotoPage(val context: Context, val data: Illust) {
    val view = LayoutInflater.from(context).inflate(R.layout.layout_photo_view, null)

    val mBinding = DataBindingUtil.bind<LayoutPhotoViewBinding>(view)

    val viewModel = PhotoViewModel(data)

    init {
        mBinding?.let {
            it.vm = viewModel
            it.errorView.reload.setOnClickListener {
                load()
            }
        }
        load()
    }

    fun load() {
        if (mBinding != null) {
            viewModel.loadState.set(LoadState.Loading)
            GlideApp.with(context)
                    .asBitmap()
                    .load(data.image_urls.original)
                    .listener(object : RequestListener<Bitmap> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                            viewModel.loadState.set(LoadState.Failed(e
                                    ?: ApiException(ApiException.CODE_UNKNOWN)))
                            return false
                        }

                        override fun onResourceReady(resource: Bitmap, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            viewModel.loadState.set(LoadState.Succeed)
                            return false
                        }

                    })
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onLoadCleared(placeholder: Drawable?) {
                            viewModel.cover = null
                        }

                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            viewModel.cover = resource
                            mBinding.cover.setImageBitmap(resource)
                        }

                    })
        }

    }

    fun destroy() {
        mBinding?.let { GlideApp.with(it.cover).clear(it.cover) }
    }
}