package com.lyj.fakepivix.network.retrofit

import com.lyj.fakepivix.constant.Constant
import okhttp3.internal.Internal.instance
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author 19930
 * @date 2018/12/21
 * @desc
 */
class RetrofitManager private constructor() {

    private var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Constant.Net.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    companion object {
        val instance: RetrofitManager by lazy {
            RetrofitManager()
        }
    }

}
