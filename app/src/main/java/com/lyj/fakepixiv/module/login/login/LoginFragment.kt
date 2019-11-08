package com.lyj.fakepixiv.module.login.login

import android.os.Bundle
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.FragmentationFragment
import com.lyj.fakepixiv.app.databinding.onPropertyChangedCallback
import com.lyj.fakepixiv.app.network.ApiException
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.app.utils.ToastUtil
import com.lyj.fakepixiv.app.utils.finish
import com.lyj.fakepixiv.app.utils.startActivity
import com.lyj.fakepixiv.databinding.FragmentLoginBinding
import com.lyj.fakepixiv.module.login.register.RegisterFragment
import com.lyj.fakepixiv.module.main.MainActivity
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
        with(mViewModel) {
            loginState.addOnPropertyChangedCallback(onPropertyChangedCallback {
                _, _ ->
                when (loginState.get()) {
                    is LoadState.Loading -> showLoadingDialog()
                    is LoadState.Succeed -> {
                        hideLoadingDialog()
                        startActivity(MainActivity::class.java)
                        finish()
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