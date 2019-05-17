package com.lyj.fakepivix

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.cache.DiskCache
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemoryCache
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.bumptech.glide.util.FixedPreloadSizeProvider
import com.lyj.fakepivix.app.App
import com.lyj.fakepivix.app.network.retrofit.interceptors.LoggerInterceptor
import com.lyj.fakepivix.app.network.retrofit.RetrofitManager
import okhttp3.OkHttpClient
import java.io.File
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
        super.applyOptions(context,    builder)
        builder
                .setDiskCache {
                    DiskLruCacheFactory(App.context.externalCacheDir.absolutePath+File.separator+"glide_image", DiskLruCacheFactory.DEFAULT_DISK_CACHE_SIZE.toLong())
                            .build()
                }
                .setLogLevel(Log.DEBUG)
                .setDefaultRequestOptions(RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).format(DecodeFormat.DEFAULT))
                //.setDefaultTransitionOptions(Drawable::class.java, DrawableTransitionOptions.withCrossFade())

    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(client))
    }
}