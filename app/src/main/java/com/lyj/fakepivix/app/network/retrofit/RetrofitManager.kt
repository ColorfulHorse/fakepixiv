package com.lyj.fakepivix.app.network.retrofit

import com.lyj.fakepivix.app.constant.Constant
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**.
 *
 * @author 19930
 * @date 2018/12/21
 * @desc
 */
class RetrofitManager private constructor() {

    private val services:MutableMap<String, Any> = mutableMapOf()

    private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Constant.Net.BASE_URL)
            .client(OkHttpClient
                    .Builder()
                    .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .addInterceptor(LoggerInterceptor())
                    .build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    companion object {
        const val TIME_OUT = 10L
        val instance: RetrofitManager by lazy {
            RetrofitManager()
        }
    }


    @Synchronized
    fun <T: Any> obtainService(clazz: Class<T>) : T  {
        val service: T
        if (!services.contains(clazz.name)) {
            service = retrofit.create(clazz)
            services[clazz.name] = service
        }else{
            service = services[clazz.name] as T
        }
        return service
    }


}
