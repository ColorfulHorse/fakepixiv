package com.lyj.fakepivix.app.base

import android.databinding.ViewDataBinding
import android.os.Bundle
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
abstract class BackFragment<V : ViewDataBinding, VM : BaseViewModel<out IModel>> : FragmentationFragment<V, VM>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        mToolbar?.let {
            it.setNavigationIcon(R.drawable.md_nav_back)
            it.setNavigationOnClickListener {
                pop()
            }
        }
        return view
    }
}