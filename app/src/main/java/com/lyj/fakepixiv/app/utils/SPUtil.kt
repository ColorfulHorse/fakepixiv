package com.lyj.fakepixiv.app.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.lyj.fakepixiv.app.application.ApplicationLike
import com.lyj.fakepixiv.app.constant.Constant
import com.lyj.fakepixiv.app.data.model.response.LoginData

/**
 * @author greensun
 *
 * @date 2019/4/11
 *
 * @desc
 */
object SPUtil {
    private const val DEFAULT_SP = "DEFAULT_SP"
    private const val KEY_SEARCH = "KEY_SEARCH"
    private val sp: SharedPreferences by lazy { ApplicationLike.context.getSharedPreferences(DEFAULT_SP, Context.MODE_PRIVATE) }


    /**
     * 保存登录信息
     */
    fun saveLoginData(loginData: LoginData) {
        val str = JsonUtil.bean2Json(loginData)
        sp.edit().putString(Constant.SP.KEY_LOGIN_CACHE, str)
                .apply()
    }

    fun getLoginData(): LoginData? {
        val str = sp.getString(Constant.SP.KEY_LOGIN_CACHE, "")
        if (str != null) {
            if (str.isNotEmpty()) {
                return JsonUtil.json2Bean(str)
            }
        }
        return null
    }

    fun clearLoginData() {
        sp.edit().putString(Constant.SP.KEY_LOGIN_CACHE, "")
                .apply()
    }

    fun saveSearchHistory(value: String) {
        val mutableSet = sp.getStringSet(KEY_SEARCH, mutableSetOf())
        mutableSet?.let {
            if (mutableSet.size >= 50) {
                mutableSet.remove(mutableSet.first())
            }
            mutableSet.add(value)
        }
        sp.edit().putStringSet(KEY_SEARCH, mutableSet).apply()
    }

    fun removeSearchHistory(value: String) {
        val mutableSet = sp.getStringSet(KEY_SEARCH, mutableSetOf())
        mutableSet?.remove(value)
        sp.edit().putStringSet(KEY_SEARCH, mutableSet).apply()
    }

    fun removeAllSearchHistory() {
        sp.edit().putStringSet(KEY_SEARCH, null).apply()
    }

    fun getSearchHistory() = sp.getStringSet(KEY_SEARCH, mutableSetOf())?:mutableSetOf()

    fun save(key: String, value: Any) {
        sp.edit {
            putString(key, JsonUtil.bean2Json(value))
        }
    }

    inline fun <reified T> getObj(key: String): T? {
        val sp = ApplicationLike.context.getSharedPreferences("DEFAULT_SP", Context.MODE_PRIVATE)
        val str = sp.getString(key, "")?:""
        return JsonUtil.json2Bean<T>(str)
    }

    fun save(key: String, value: String) {
        sp.edit().putString(key, value)
                .apply()
    }

    fun save(key: String, value: Int) {
        sp.edit().putInt(key, value)
                .apply()
    }

    fun get(key: String, defValue: String): String {
        return sp.getString(key, defValue)?:""
    }

    fun get(key: String, defValue: Int = -1): Int {
        return sp.getInt(key, defValue)
    }
}