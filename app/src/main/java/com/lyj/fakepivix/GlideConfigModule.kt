package com.lyj.fakepivix

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.lyj.fakepivix.app.network.retrofit.interceptors.LoggerInterceptor
import com.lyj.fakepivix.app.network.retrofit.RetrofitManager
import okhttp3.OkHttpClient
import java.io.InputStream
import java.util.concurrent.TimeUnit

/**
 * @author greensun
 *
 * @date 2019/3/24
 *
 * @desc
 */
@GlideModule
class GlideConfigModule : AppGlideModule() {

    val client = OkHttpClient
            .Builder()
            .connectTimeout(RetrofitManager.TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(RetrofitManager.TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(LoggerInterceptor())
            .build()

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        builder
                .setDefaultRequestOptions(RequestOptions().format(DecodeFormat.DEFAULT))
                //.setDefaultTransitionOptions(Drawable::class.java, DrawableTransitionOptions.withCrossFade())

    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(client))
    }
}