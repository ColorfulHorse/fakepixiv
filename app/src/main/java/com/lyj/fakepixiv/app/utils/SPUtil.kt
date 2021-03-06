package com.lyj.fakepixiv.app.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.lyj.fakepixiv.app.application.AppDelegate
import com.lyj.fakepixiv.app.application.ApplicationLike
import com.lyj.fakepixiv.app.constant.Constant
import com.lyj.fakepixiv.app.data.model.response.LoginData
import com.lyj.fakepixiv.app.utils.JsonUtil.fromJson
import com.lyj.fakepixiv.app.utils.JsonUtil.toJson

/**
 * @author greensun
 *
 * @date 2019/4/11
 *
 * @desc
 */
object SPUtil {
    private const val DEFAULT_SP = "DEFAULT_SP"
    val sp: SharedPreferences by lazy { ApplicationLike.context.getSharedPreferences(DEFAULT_SP, Context.MODE_PRIVATE) }

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

    fun saveSearchHistory(value: String): Boolean {
        var exists = false
        val mutableSet = sp.getStringSet(Constant.SP.KEY_SEARCH, mutableSetOf())
        mutableSet?.let {
            if (mutableSet.size >= 50) {
                mutableSet.remove(mutableSet.first())
            }
            if (mutableSet.contains(value)) {
                exists = true
                mutableSet.remove(value)
            }
            mutableSet.add(value)
        }
        sp.edit().putStringSet(Constant.SP.KEY_SEARCH, mutableSet).apply()
        return exists
    }

    fun removeSearchHistory(value: String) {
        val mutableSet = sp.getStringSet(Constant.SP.KEY_SEARCH, mutableSetOf())
        mutableSet?.remove(value)
        sp.edit().putStringSet(Constant.SP.KEY_SEARCH, mutableSet).apply()
    }

    fun removeAllSearchHistory() {
        sp.edit().putStringSet(Constant.SP.KEY_SEARCH, null).apply()
    }

    fun getSearchHistory() = sp.getStringSet(Constant.SP.KEY_SEARCH, mutableSetOf())
            ?: mutableSetOf()

    fun <T> save(key: String, value: T) {
        sp.edit {
            when (value) {
                is Long -> putLong(key, value)
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                else -> throw TypeCastException("do not support this type")
            }
        }
    }

    fun <T> get(key: String, defValue: T): T = with(sp) {
        val res = when (defValue) {
            is Long -> getLong(key, defValue)
            is String -> getString(key, defValue) ?: ""
            is Int -> getInt(key, defValue)
            is Boolean -> getBoolean(key, defValue)
            is Float -> getFloat(key, defValue)
            else -> throw TypeCastException("do not support this type")
        }
        res as T
    }


    inline fun <reified T> saveObj(key: String, value: T) {
        sp.edit {
            putString(key, value.toJson())
        }
    }

    inline fun <reified T> getObj(key: String): T? {
        val str = sp.getString(key, "") ?: ""
        return str.fromJson()
    }
}