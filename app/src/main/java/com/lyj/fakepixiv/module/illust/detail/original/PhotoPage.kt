package com.lyj.fakepixiv.module.illust.detail.original

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.lyj.fakepixiv.GlideApp
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.network.ApiException
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.app.reactivex.io
import com.lyj.fakepixiv.databinding.LayoutPhotoViewBinding
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.layout_error.view.*
import java.io.File

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

    var disposable: Disposable? = null

    init {
        mBinding?.let {
            it.vm = viewModel
            it.errorView.reload.setOnClickListener {
                load()
            }
        }
        load()
    }

    /**
     * 下载图片文件到内存并展示
     */
    fun load() {
        if (mBinding != null) {
            disposable = Observable.create<File> {
                val file = GlideApp.with(context)
                        .downloadOnly()
                        .load(data.image_urls.original)
                        .submit()
                        .get()
                it.onNext(file)
                it.onComplete()
            }
                    .doOnSubscribe { viewModel.loadState.set(LoadState.Loading) }
                    .io()
                    .doOnNext {
                        GlideApp.with(context)
                                .load(it)
                                .format(DecodeFormat.PREFER_RGB_565)
                                .override(Target.SIZE_ORIGINAL)
                                .listener(object : RequestListener<Drawable> {
                                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                        viewModel.loadState.set(LoadState.Failed(e
                                                ?: ApiException(ApiException.CODE_UNKNOWN)))
                                        return false
                                    }

                                    override fun onResourceReady(resource: Drawable, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                        viewModel.loadState.set(LoadState.Succeed)
                                        return false
                                    }

                                })
                                .into(mBinding.cover)
                    }
                    .subscribeBy(onNext = {
                        viewModel.file = it
                    }, onError = {
                        viewModel.loadState.set(LoadState.Failed(it))
                    })
        }

    }

    fun destroy() {
        disposable?.dispose()
        mBinding?.let { GlideApp.with(it.cover).clear(it.cover) }
    }
}