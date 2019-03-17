package com.lyj.fakepivix.app.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import androidx.annotation.LayoutRes

/**
 * @author greensun
 *
 * @date 2019/3/16
 *
 * @desc
 */
abstract class BaseActivity<V : ViewDataBinding, VM : BaseViewModel<out IModel>> : AppCompatActivity() {
    protected lateinit var mBinding: V
    protected abstract var mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(mViewModel)
        mBinding = DataBindingUtil.setContentView(this, bindLayout())
        initData()
        initView()
    }

    abstract fun initData()

    abstract fun initView()

    @LayoutRes
    abstract fun bindLayout() : Int
}
