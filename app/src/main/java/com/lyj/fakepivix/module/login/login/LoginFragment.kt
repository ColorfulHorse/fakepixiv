package com.lyj.fakepivix.module.login.login

import android.databinding.Observable
import android.os.Bundle
import com.gyf.barlibrary.ImmersionBar
import com.lyj.fakepivix.R
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.databinding.FragmentLoginBinding
import com.lyj.fakepivix.module.login.register.RegisterFragment
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * @author greensun
 *
 * @date 2019/3/20
 *
 * @desc
 */
class LoginFragment : FragmentationFragment<FragmentLoginBinding, LoginViewModel>() {

    companion object {
        fun newInstance(): LoginFragment {
            val args = Bundle()
            val fragment = LoginFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override var mViewModel: LoginViewModel = LoginViewModel()


    override fun init(savedInstanceState: Bundle?) {
        btn_register.setOnClickListener {
            start(RegisterFragment.newInstance())
        }
        mViewModel.loading.addOnPropertyChangedCallback(object: Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val loading = mViewModel.loading.get()
                loading?.let {
                    if (it) {

                    } else {

                    }
                }
            }
        })

    }


    override fun onKeyboardChanged(isOpen: Boolean, height: Int) {
        mViewModel.keyboardOpened.set(isOpen)
    }

    override fun bindLayout(): Int = R.layout.fragment_login

}