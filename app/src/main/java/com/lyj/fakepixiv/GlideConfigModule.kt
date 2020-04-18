package com.lyj.fakepixiv

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.lyj.fakepixiv.app.application.ApplicationLike
import com.lyj.fakepixiv.app.network.retrofit.RetrofitManager
import com.lyj.fakepixiv.app.network.retrofit.interceptors.LoggerInterceptor
import com.lyj.fakepixiv.app.utils.DefaultFormatPrinter
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
    companion object {
        private const val MAP_KEY = "Referer"
        private const val IMAGE_REFERER = "https://app-api.pixiv.net/"
    }

    val client = OkHttpClient
            .Builder()
            .connectTimeout(RetrofitManager.TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(RetrofitManager.TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor {
                val req = it.request()
                        .newBuilder()
                        .addHeader(MAP_KEY, IMAGE_REFERER)
                        .build()
                it.proceed(req)
            }
            .addInterceptor(LoggerInterceptor(DefaultFormatPrinter("GlideHttpLog")))
            .build()

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        builder
                .setDiskCache {
                    val dir = ApplicationLike.context.externalCacheDir?:ApplicationLike.context.filesDir
                    DiskLruCacheFactory(dir.absolutePath+File.separator+"glide_image", DiskLruCacheFactory.DEFAULT_DISK_CACHE_SIZE.toLong())
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