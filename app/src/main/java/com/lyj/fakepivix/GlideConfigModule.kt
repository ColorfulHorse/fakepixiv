package com.lyj.fakepivix

import android.content.Context
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.TransitionOptions
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.executor.GlideExecutor
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.lyj.fakepivix.app.network.retrofit.RetrofitManager
import java.io.InputStream

/**
 * @author greensun
 *
 * @date 2019/3/24
 *
 * @desc
 */
@GlideModule
class GlideConfigModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        builder
                .setDefaultRequestOptions(RequestOptions().format(DecodeFormat.DEFAULT))
                //.setDefaultTransitionOptions(Drawable::class.java, DrawableTransitionOptions.withCrossFade())

    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(RetrofitManager.instance.client))
    }
}