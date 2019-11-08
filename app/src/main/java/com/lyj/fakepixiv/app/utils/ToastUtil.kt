package com.lyj.fakepixiv.app.utils

import android.os.Handler
import androidx.annotation.StringRes
import android.widget.Toast
import com.lyj.fakepixiv.app.application.ApplicationLike

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
                mToast = Toast.makeText(ApplicationLike.context, content, Toast.LENGTH_SHORT)
            }
            mToast?.show()
        }
    }

     fun showToast(@StringRes resId: Int, vararg args:Any = emptyArray()) {
        if (args.isEmpty()) {
            showToast(ApplicationLike.context.getString(resId))
        }else {
            showToast(ApplicationLike.context.getString(resId, args))
        }
    }
}