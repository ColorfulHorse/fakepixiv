package com.lyj.fakepivix.app.base

import android.arch.lifecycle.LifecycleObserver
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import androidx.annotation.LayoutRes
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R

/**
 * @author greensun
 *
 * @date 2019/3/16
 *
 * @desc
 */
abstract class BaseActivity<V : ViewDataBinding, VM : BaseViewModel<out IModel?>?> : AppCompatActivity() {
    protected lateinit var mBinding: V
    protected abstract val mViewModel: VM
    protected var mToolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel?.let {
            lifecycle.addObserver(mViewModel as LifecycleObserver)
        }
        mBinding = DataBindingUtil.setContentView(this, bindLayout())
        mBinding.setVariable(bindViewModel(), mViewModel)
        mToolbar = mBinding.root.findViewById(bindToolbar())

    }


    override fun onDestroy() {
        super.onDestroy()
        mBinding.unbind()
    }

    @LayoutRes
    abstract fun bindLayout() : Int

    open fun bindViewModel() : Int = BR.vm

    open fun bindToolbar() : Int = R.id.toolbar
}
