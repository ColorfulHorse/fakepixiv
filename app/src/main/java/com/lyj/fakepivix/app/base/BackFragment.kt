package com.lyj.fakepivix.app.base

import android.databinding.ViewDataBinding
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lyj.fakepivix.R

/**
 * @author greensun
 *
 * @date 2019/3/24
 *
 * @desc
 */
abstract class BackFragment<V : ViewDataBinding, VM : BaseViewModel<out IModel?>?> : FragmentationFragment<V, VM>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        mToolbar?.let {
            it.navigationIcon = bindBackIcon()
            it.setNavigationOnClickListener {
                if (keyboardOpen) {
                    hideSoftInput()
                }else {
                    back()
                }
            }
        }
        return view
    }


    open fun bindBackIcon(): Drawable {
        return resources.getDrawable(R.drawable.ic_arrow_back_white)
    }

    open fun back() {
        pop()
    }
}