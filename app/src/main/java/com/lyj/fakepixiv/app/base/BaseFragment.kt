package com.lyj.fakepixiv.app.base

import androidx.lifecycle.LifecycleObserver
import android.content.res.Configuration
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.components.ImmersionFragment
import androidx.databinding.library.baseAdapters.BR
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.application.ApplicationLike
import com.lyj.fakepixiv.app.constant.Constant
import com.lyj.fakepixiv.app.utils.SPUtil
import com.lyj.fakepixiv.app.utils.statusBarHeight


/**
 * @author greensun
 *
 * @date 2019/3/19
 *
 * @desc
 */
abstract class BaseFragment<V : ViewDataBinding, VM : BaseViewModel?> : ImmersionFragment() {

    protected lateinit var mBinding: V
    abstract var mViewModel: VM
    protected val bindingList: MutableList<ViewDataBinding> = mutableListOf()
    protected var mToolbar: Toolbar? = null
    private var mImmersionBar: ImmersionBar? = null

    protected var keyboardOpen = false
    // 是否切换了横竖屏
    protected var diffOrientation = false

    var initializer: (() -> Unit)? = null

    protected open var keyboardListener: ((Boolean, Int) -> Unit)? = { isOpen, height ->
        keyboardOpen = isOpen
        onKeyboardChanged(isOpen, height)
    }

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
        mToolbar?.overflowIcon = ContextCompat.getDrawable(ApplicationLike.context, R.drawable.ic_more)
        mBinding.root.isClickable = true
        onCreated = true
        return mBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (immersionBarEnabled()) {
            ImmersionBar.destroy(this)
        }
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
        mViewModel?.let {
            if (it != null) {
                if (!it.lazyCreated) {
                    it.lazyInit()
                }
            }
        }

    }

    abstract fun init(savedInstanceState: Bundle?)

    override fun initImmersionBar() {
        var immersionBar = ImmersionBar.with(this)
        mToolbar?.let {
            immersionBar = immersionBar.titleBar(mToolbar)
        }
        immersionBar.keyboardEnable(true, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                    .setOnKeyboardListener(keyboardListener)
                    .init()
    }

    protected fun addSubBinding(binding: ViewDataBinding) {
        binding.setLifecycleOwner(this)
        bindingList.add(binding)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        diffOrientation = !diffOrientation
    }


    /**
     * 软键盘监听
     */
    open fun onKeyboardChanged(isOpen: Boolean, height: Int) {
        keyboardOpen = isOpen
        if (isOpen) {
            SPUtil.save(Constant.SP.KEY_KEYBOARD_HEIGHT, height)
        }
    }

    @LayoutRes
    abstract fun bindLayout() : Int

    open fun bindViewModel() : Int = BR.vm

    open fun bindToolbar() : Int = R.id.toolbar

}