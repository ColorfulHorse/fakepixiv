package com.lyj.fakepixiv.widget

import android.content.Context
import android.util.AttributeSet
import android.view.DragEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.lifecycle.Lifecycle
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.network.ApiException
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.app.utils.doOnPropertyChanged
import okhttp3.Call

/**
 * @author green sun
 *
 * @date 2019/10/16
 *
 * @desc
 */
class StateView : FrameLayout {

    var loadState: ObservableField<LoadState>? = null
        set(value) {
            field = value
            changeState()
            value?.doOnPropertyChanged(lifecycle) { _, _ ->
                changeState()
            }
        }

    private fun changeState() {
        val state = loadState?.get()
        state?.let {
            when (it) {
                is LoadState.Loading -> {
                    visibility = VISIBLE
                    setView(loadingRes)
                }
                is LoadState.Failed -> {
                    visibility = VISIBLE
                    error = it.error
                }
                else -> {
                    visibility = GONE
                }
            }
        }
    }

    var lifecycle: Lifecycle? = null

    var error: Throwable? = null
        set(value) {
            field = value
            if (value is ApiException) {
                when (value.code) {
                    ApiException.CODE_EMPTY_DATA -> setView(noneDataRes)
                    ApiException.CODE_NOT_VISIBLE -> setView(R.layout.layout_error_not_visible)
                    else -> setView(errorRes)
                }
            } else {
                setView(errorRes)
            }
        }

    var reload: (() -> Unit)? = null

    var loadingRes = R.layout.layout_common_loading_white
    var errorRes = R.layout.layout_error
    var noneDataRes = R.layout.layout_no_data

    constructor(context: Context, lifecycle: Lifecycle? = null) : super(context) {
        this.lifecycle = lifecycle
        initView(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet?) {
        attrs?.let {
            val res = context.obtainStyledAttributes(attrs, R.styleable.StateView)
            loadingRes = res.getResourceId(R.styleable.StateView_loadingLayout, R.layout.layout_common_loading_white)
            errorRes = res.getResourceId(R.styleable.StateView_errorLayout, R.layout.layout_error)
            noneDataRes = res.getResourceId(R.styleable.StateView_noneDataLayout, R.layout.layout_error_no_result)
            res.recycle()
        }
        setView(loadingRes)
        visibility = View.GONE
    }

     fun setView(@LayoutRes layoutId: Int) {
        removeAllViews()
        View.inflate(context, layoutId, this)
        findViewById<TextView>(R.id.reload)?.setOnClickListener {
            reload?.invoke()
        }
    }

}

@BindingAdapter(value = ["state", "reload"], requireAll = false)
fun StateView.state(state: ObservableField<LoadState>, reload: (() -> Unit)? = null) {
    this.loadState = state
    this.reload = reload
}

