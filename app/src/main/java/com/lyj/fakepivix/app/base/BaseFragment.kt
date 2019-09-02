package com.lyj.fakepivix.app.base

import android.arch.lifecycle.LifecycleObserver
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes
import com.gyf.barlibrary.ImmersionBar
import com.gyf.barlibrary.ImmersionFragment
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.App
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportHelper.hideSoftInput
import kotlin.properties.Delegates


/**
 * @author greensun
 *
 * @date 2019/3/19
 *
 * @desc
 */
abstract class BaseFragment<V : ViewDataBinding, VM : BaseViewModel<out IModel?>?> : ImmersionFragment() {

    protected lateinit var mBinding: V
    abstract var mViewModel: VM
    protected val bindingList: MutableList<ViewDataBinding> = mutableListOf()
    protected var mToolbar: Toolbar? = null
    private var mImmersionBar: ImmersionBar? = null

    protected var keyboardOpen = false
    // 是否切换了横竖屏
    protected var diffOrientation = false

    var initializer: (() -> Unit)? = null

    protected var onCreated = false
    protected var lazyCreated = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, bindLayout(), container, false)
        mBinding.setLifecycleOwner(this)
        mViewModel?.let {
            lifecycle.addObserver(it as LifecycleObserver)
            mBinding.setVariable(bindViewModel(), it)
        }
        mToolbar = mBinding.root.findViewById(bindToolbar())
        mToolbar?.overflowIcon = ContextCompat.getDrawable(App.context, R.drawable.ic_more)
        mBinding.root.isClickable = true
        onCreated = true
        return mBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bindingList.forEach { it.unbind() }
        mBinding.unbind()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init(savedInstanceState)
        initializer?.invoke()
    }

    protected fun lazyInit() {
        lazyCreated = true
        mViewModel?.lazyCreated = true
    }

    abstract fun init(savedInstanceState: Bundle?)

    override fun initImmersionBar() {
        var immersionBar = ImmersionBar.with(this)
        mToolbar?.let {
            immersionBar = immersionBar.titleBar(mToolbar)
        }
        immersionBar.keyboardEnable(true, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                    .setOnKeyboardListener{isOpen, height ->
                        keyboardOpen = isOpen
                        onKeyboardChanged(isOpen, height)
                    }
                    .init()
    }

    protected fun addSubBinding(binding: ViewDataBinding) {
        binding.setLifecycleOwner(this)
        bindingList.add(binding)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        diffOrientation = !diffOrientation
    }


    /**
     * 软键盘监听
     */
    open fun onKeyboardChanged(isOpen: Boolean, height: Int) {
        keyboardOpen = isOpen
    }

    @LayoutRes
    abstract fun bindLayout() : Int

    open fun bindViewModel() : Int = BR.vm

    open fun bindToolbar() : Int = R.id.toolbar

}