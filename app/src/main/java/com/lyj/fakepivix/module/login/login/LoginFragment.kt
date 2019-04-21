package com.lyj.fakepivix.module.login.login

import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.databinding.OnPropertyChangedCallbackImp
import com.lyj.fakepivix.app.network.ApiException
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.app.utils.ToastUtil
import com.lyj.fakepivix.app.utils.startActivity
import com.lyj.fakepivix.databinding.FragmentLoginBinding
import com.lyj.fakepivix.module.login.register.RegisterFragment
import com.lyj.fakepivix.module.main.MainActivity
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking

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
    var dialog: AlertDialog? = null

    override fun init(savedInstanceState: Bundle?) {
        btn_register.setOnClickListener {
            start(RegisterFragment.newInstance())
        }

        with(mViewModel) {
            loginState.addOnPropertyChangedCallback(OnPropertyChangedCallbackImp {
                _, _ ->
                when (loginState.get()) {
                    is LoadState.Loading -> showLoadingDialog()
                    is LoadState.Succeed -> {
                        hideLoadingDialog()
                        context?.startActivity(MainActivity::class.java)
                    }
                    is LoadState.Failed -> {
                        hideLoadingDialog()
                        with(loginState.get() as LoadState.Failed) {
                            if (error is ApiException) {
                                ToastUtil.showToast(error.message)
                            }
                        }
                    }
                }
            })
        }
    }

    private fun showLoadingDialog() {
//        dialog = AlertDialog.Builder(mActivity, R.style.theme_action_dialog)
//                .setView(R.layout.common_loading_dialog)
//                .setCancelable(false)
//                .show()
//        dialog?.window?.let {
//            val width = resources.displayMetrics.widthPixels/3*2
//            val height = width * 0.65f
//            val lp = it.attributes
//            lp.width = width
//            lp.height = height.toInt()
//            it.attributes = lp
//        }
    }

    private fun hideLoadingDialog() {
        //dialog?.dismiss()
    }


    override fun onKeyboardChanged(isOpen: Boolean, height: Int) {
        mViewModel.keyboardOpened.set(isOpen)
    }

    override fun bindLayout(): Int = R.layout.fragment_login

}