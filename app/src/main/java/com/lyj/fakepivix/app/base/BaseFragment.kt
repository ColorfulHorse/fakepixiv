package com.lyj.fakepivix.app.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import com.lyj.fakepivix.BR
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.lyj.fakepivix.module.login.login.LoginFragment

/**
 * @author greensun
 *
 * @date 2019/3/19
 *
 * @desc
 */
abstract class BaseFragment<V : ViewDataBinding, VM : BaseViewModel<out IModel>> : Fragment() {

    protected lateinit var mBinding: V
    protected abstract var mViewModel: VM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lifecycle.addObserver(mViewModel)
        mBinding = DataBindingUtil.inflate(inflater, bindLayout(), container, false)
        mBinding.setVariable(bindViewModel(), mViewModel)
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData(savedInstanceState)
        initView(savedInstanceState)
    }

    abstract fun initData(savedInstanceState: Bundle?)

    abstract fun initView(savedInstanceState: Bundle?)

    @LayoutRes
    abstract fun bindLayout() : Int

    open fun bindViewModel() : Int = BR.vm
}