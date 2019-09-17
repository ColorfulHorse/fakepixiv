package com.lyj.fakepivix.app.base

import android.arch.lifecycle.LifecycleObserver
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.lyj.fakepivix.BR

/**
 * @author greensun
 *
 * @date 2019/9/16
 *
 * @desc
 */
abstract class BaseDialogFragment<V : ViewDataBinding, VM : BaseViewModel<out IModel?>?> : DialogFragment() {

    protected lateinit var mBinding: V

    abstract var mViewModel: VM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, bindLayout(), container, false)
        mBinding.setLifecycleOwner(this)
        mViewModel?.let {
            lifecycle.addObserver(it as LifecycleObserver)
            mBinding.setVariable(bindViewModel(), it)
        }
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init(savedInstanceState)
    }

    abstract fun init(savedInstanceState: Bundle?)

    @LayoutRes
    abstract fun bindLayout() : Int

    open fun bindViewModel() : Int = BR.vm
}