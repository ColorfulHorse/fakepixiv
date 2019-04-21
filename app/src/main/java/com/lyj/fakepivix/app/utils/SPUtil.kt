package com.lyj.fakepivix.app.utils

import android.content.Context
import android.content.SharedPreferences
import com.lyj.fakepivix.app.App
import com.lyj.fakepivix.app.constant.Constant
import com.lyj.fakepivix.app.data.model.response.LoginData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * @author greensun
 *
 * @date 2019/4/11
 *
 * @desc
 */
object SPUtil {
    private const val DEFAULT_SP = "DEFAULT_SP"
    private val moshi: Moshi by lazy { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() }
    private val sp: SharedPreferences by lazy { App.context.getSharedPreferences(DEFAULT_SP, Context.MODE_PRIVATE) }


    fun saveLoginData(loginData: LoginData) {
        val adapter = moshi.adapter(LoginData::class.java)
        val str = adapter.toJson(loginData)
        sp.edit().putString(Constant.SP.KEY_LOGIN_CACHE, str)
                .apply()
    }

    fun getLoginData(): LoginData? {
        val str = sp.getString(Constant.SP.KEY_LOGIN_CACHE, "")
        if (str != null) {
            if (str.isNotEmpty()) {
                val adapter = moshi.adapter(LoginData::class.java)
                return adapter.fromJson(str)
            }
        }
        return null
    }
}