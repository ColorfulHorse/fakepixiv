package com.lyj.fakepixiv.module.sample

import android.os.Bundle
import android.view.WindowManager
import androidx.databinding.ViewDataBinding
import com.gyf.immersionbar.ImmersionBar
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.base.FragmentationFragment

/**
 * @author greensun
 *
 * @date 2019/3/20
 *
 * @desc
 */
class EmptyFragment : FragmentationFragment<ViewDataBinding, BaseViewModel?>() {

    companion object {
        fun newInstance() = EmptyFragment()
    }

    override fun init(savedInstanceState: Bundle?) {
    }

//    override fun immersionBarEnabled(): Boolean {
//        return false
//    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .transparentStatusBar()
                .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                .keyboardEnable(true)
                .init()
    }

    override fun bindLayout(): Int = R.layout.activity_test

    override var mViewModel: BaseViewModel? = null
}