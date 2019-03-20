package com.lyj.fakepivix.module.login.register

import android.os.Bundle
import com.gyf.barlibrary.ImmersionBar
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.databinding.FragmentRegisterBinding
import kotlinx.android.synthetic.main.fragment_register.*

/**
 * @author greensun
 *
 * @date 2019/3/20
 *
 * @desc
 */
class RegisterFragment : FragmentationFragment<FragmentRegisterBinding, RegisterViewModel>() {

    companion object {
        fun newInstance(): RegisterFragment {
            val args = Bundle()
            val fragment = RegisterFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override var mViewModel: RegisterViewModel = RegisterViewModel()

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.setTitleBar(mActivity, toolbar)
    }

    override fun bindLayout(): Int = R.layout.fragment_register

}