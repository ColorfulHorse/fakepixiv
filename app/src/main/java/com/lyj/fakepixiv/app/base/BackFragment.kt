package com.lyj.fakepixiv.app.base

import android.databinding.ViewDataBinding
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lyj.fakepixiv.R

/**
 * @author greensun
 *
 * @date 2019/3/24
 *
 * @desc
 */
abstract class BackFragment<V : ViewDataBinding, VM : BaseViewModel?> : FragmentationFragment<V, VM>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        mToolbar?.let {
            it.navigationIcon = bindBackIcon()
            it.setNavigationOnClickListener {
                back()
            }
        }
        return view
    }


    open fun bindBackIcon(): Drawable {
        return resources.getDrawable(R.drawable.ic_arrow_back_white)
    }

    fun createDefaultBack() =
        DrawerArrowDrawable(mActivity).apply {
            progress = 1F
            color = Color.WHITE
        }


    open fun back() {
        if (keyboardOpen) {
            hideSoftInput()
        }else {
            pop()
        }
    }
}