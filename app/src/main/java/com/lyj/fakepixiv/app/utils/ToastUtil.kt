package com.lyj.fakepixiv.app.utils

import android.os.Handler
import android.support.annotation.StringRes
import android.widget.Toast
import com.lyj.fakepixiv.app.App

/**
 * @author greensun
 *
 * @date 2019/4/12
 *
 * @desc
 */
object ToastUtil {
    private var mToast: Toast? = null
    private val mHandler = Handler()

    fun showToast(content: String) {
        mHandler.post {
            if (mToast != null) {
                mToast?.setText(content)
            } else {
                mToast = Toast.makeText(App.context, content, Toast.LENGTH_SHORT)
            }
            mToast?.show()
        }
    }

     fun showToast(@StringRes resId: Int, vararg args:Any = emptyArray()) {
        if (args.isEmpty()) {
            showToast(App.context.getString(resId))
        }else {
            showToast(App.context.getString(resId, args))
        }
    }
}