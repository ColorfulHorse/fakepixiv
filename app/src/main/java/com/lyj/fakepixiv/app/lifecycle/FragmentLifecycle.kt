package com.lyj.fakepixiv.app.lifecycle

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import com.lyj.fakepixiv.app.base.BaseFragment
import com.lyj.fakepixiv.app.base.ViewModelProvider

/**
 * @author greensun
 *
 * @date 2019/7/23
 *
 * @desc
 */
object FragmentLifecycle : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
        super.onFragmentViewCreated(fm, f, v, savedInstanceState)
        if (f is BaseFragment<*,*>) {
            f.mViewModel?.let {
                ViewModelProvider[f.hashCode()] = it
            }
        }
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        super.onFragmentViewDestroyed(fm, f)
        if (f is BaseFragment<*,*>) {
            f.mViewModel?.let {
                ViewModelProvider - f.hashCode()
            }
        }
    }


    override fun onFragmentActivityCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        super.onFragmentActivityCreated(fm, f, savedInstanceState)
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        super.onFragmentPaused(fm, f)
    }

}