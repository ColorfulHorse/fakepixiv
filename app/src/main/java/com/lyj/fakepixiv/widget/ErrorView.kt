package com.lyj.fakepixiv.widget

import android.content.Context
import androidx.annotation.LayoutRes
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.network.ApiException
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import javax.net.ssl.SSLHandshakeException

/**
 * @author green sun
 *
 * @date 2019/10/16
 *
 * @desc
 */
class ErrorView : FrameLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    var reload: (() -> Unit)? = null

    fun setView(@LayoutRes layoutId: Int) {
        removeAllViews()
        View.inflate(context, layoutId, this)
        findViewById<TextView>(R.id.reload)?.setOnClickListener {
            reload?.invoke()
        }
    }

    fun setError(err: Throwable) {
        if (err is ApiException) {
            when(err.code) {
                ApiException.CODE_EMPTY_DATA -> setView(R.layout.layout_error_no_result)
                ApiException.CODE_NOT_VISIBLE -> setView(R.layout.layout_error_not_visible)
            }
        }else if (err is IOException) {
            setView(R.layout.layout_error)
        }
    }
}