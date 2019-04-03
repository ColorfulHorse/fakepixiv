package com.lyj.fakepivix.app.base

import android.arch.lifecycle.LifecycleObserver
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import com.lyj.fakepivix.BR
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.gyf.barlibrary.ImmersionBar
import com.lyj.fakepivix.R
import com.lyj.fakepivix.module.login.login.LoginFragment
import kotlinx.android.synthetic.main.fragment_register.view.*

/**
 * @author greensun
 *
 * @date 2019/3/19
 *
 * @desc
 */
abstract class BaseFragment<V : ViewDataBinding, VM : BaseViewModel<out IModel?>?> : Fragment() {

    protected lateinit var mBinding: V
    protected abstract var mViewModel: VM
    protected var mToolbar: Toolbar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mViewModel?.let {
            lifecycle.addObserver(mViewModel as LifecycleObserver)
        }
        mBinding = DataBindingUtil.inflate(inflater, bindLayout(), container, false)
        mBinding.setVariable(bindViewModel(), mViewModel)
        mToolbar = mBinding.root.findViewById(bindToolbar())
        mToolbar?.let {
            ImmersionBar.setTitleBar(activity, mToolbar)
        }
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

    open fun bindToolbar() : Int = R.id.toolbar
}