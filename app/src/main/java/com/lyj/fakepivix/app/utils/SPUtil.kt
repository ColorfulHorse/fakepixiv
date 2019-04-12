package com.lyj.fakepivix.app.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.lyj.fakepivix.app.App
import com.lyj.fakepivix.app.constant.Constant
import com.lyj.fakepivix.app.data.model.response.LoginData

/**
 * @author greensun
 *
 * @date 2019/4/11
 *
 * @desc
 */
object SPUtil {
    private const val DEFAULT_SP = "DEFAULT_SP"
    private val gson: Gson by lazy { Gson() }
    private val sp: SharedPreferences by lazy { App.context.getSharedPreferences(DEFAULT_SP, Context.MODE_PRIVATE) }


    fun saveLoginData(loginData: LoginData) {
        val str = gson.toJson(loginData, LoginData::class.java)
        sp.edit().putString(Constant.SP.KEY_LOGIN_CACHE, str)
                .apply()
    }

    fun getLoginData(): LoginData? {
        val str = sp.getString(Constant.SP.KEY_LOGIN_CACHE, "")
        if (str.isNotEmpty()) {
            return gson.fromJson(str, LoginData::class.java)
        }
        return null
    }
}