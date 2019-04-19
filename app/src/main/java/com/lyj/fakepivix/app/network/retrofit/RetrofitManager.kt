package com.lyj.fakepivix.app.network.retrofit

import com.lyj.fakepivix.app.constant.Constant
import com.lyj.fakepivix.app.network.ApiService
import com.lyj.fakepivix.app.network.retrofit.interceptors.ApiExceptionInterceptor
import com.lyj.fakepivix.app.network.retrofit.interceptors.CommonParamsInterceptor
import com.lyj.fakepivix.app.network.retrofit.interceptors.LoggerInterceptor
import com.lyj.fakepivix.app.network.retrofit.interceptors.SwitchBaseUrlInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

/**.
 *
 * @author 19930
 * @date 2018/12/21
 * @desc
 */
class RetrofitManager private constructor() {

    private val services:MutableMap<String, Any> = mutableMapOf()

    val apiService: ApiService by lazy { retrofit.create(ApiService::class.java) }

    private val client: OkHttpClient = OkHttpClient
            .Builder()
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            //.sslSocketFactory(getSSLSocketFactory(), getTrustManager())
            .addInterceptor(SwitchBaseUrlInterceptor())
            .addInterceptor(CommonParamsInterceptor())
            .addInterceptor(LoggerInterceptor())
            .addInterceptor(ApiExceptionInterceptor())
            .build()

    val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Constant.Net.BASE_URL)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    companion object {
        const val TIME_OUT = 15L
        val instance: RetrofitManager by lazy {
            RetrofitManager()
        }
    }

    private fun getTrustManager(): X509TrustManager {

        return object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>, authType: String) {

            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>, authType: String) {
                // 校验服务器证书
                if (chain.isEmpty()) {
                    throw IllegalArgumentException("服务端证书为空")
                }
                val fa = CertificateFactory.getInstance("X.509")
                var cert: X509Certificate? = null
                File("xxx")
                        .inputStream()
                        .use {
                            cert = fa.generateCertificate(it) as X509Certificate
                        }
                chain.forEach {
                    // 服务器证书和客户端预埋证书做校验
                    try {
                        it.verify(cert?.publicKey)
                    }catch (e: Exception) {

                    }
                }
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }

        }
    }

    private fun getSSLSocketFactory():SSLSocketFactory {
        val cf = CertificateFactory.getInstance("X.509")
        var ca: Certificate? = null
        File("xxx")
                .inputStream()
                .use {
                    ca = cf.generateCertificate(it)
        }
        // 创建 Keystore 包含我们的证书
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStore.load(null)
        keyStore.setCertificateEntry("anchor", ca)

        // 仅信任Keystore中的证书
        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(keyStore)

        val sslContext = SSLContext.getInstance("TSL")
        sslContext.init(null, trustManagerFactory.trustManagers, SecureRandom())
        return sslContext.socketFactory
    }

}
