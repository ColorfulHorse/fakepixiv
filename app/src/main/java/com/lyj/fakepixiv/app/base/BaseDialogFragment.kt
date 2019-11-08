package com.lyj.fakepixiv.app.base

import androidx.lifecycle.LifecycleObserver
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import com.lyj.fakepixiv.BR

/**
 * @author greensun
 *
 * @date 2019/9/16
 *
 * @desc
 */
abstract class BaseDialogFragment<V : ViewDataBinding, VM : BaseViewModel?> : DialogFragment() {

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