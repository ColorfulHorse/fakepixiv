package com.lyj.fakepixiv.module.login.register

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.BackFragment
import com.lyj.fakepixiv.app.utils.SecurityUtil
import com.lyj.fakepixiv.databinding.FragmentRegisterBinding

/**
 * @author greensun
 *
 * @date 2019/3/20
 *
 * @desc
 */
class RegisterFragment : BackFragment<FragmentRegisterBinding, RegisterViewModel>() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    override var mViewModel: RegisterViewModel = RegisterViewModel()


    override fun init(savedInstanceState: Bundle?) {
        mBinding.btnRegister.setOnClickListener {
            // https://accounts.pixiv.net/login?return_to=https://app-api.pixiv.net/web/v1/users/auth/pixiv/start?code_challenge=APu12__uuh1z4NSOYpooVymCqOVW_Ra8X4cFqRynpjc&code_challenge_method=S256&client=pixiv-android&source=pixiv-android&view_type=page&ref=&app_ios=0&booth_app=0"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(
                    "https://app-api.pixiv.net/web/v1/login?code_challenge=${SecurityUtil.getCodeVerifier()}&code_challenge_method=S256&client=pixiv-android"
            ))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
//            SecurityUtil.test()
        }
    }

    override fun onKeyboardChanged(isOpen: Boolean, height: Int) {
        mViewModel.keyboardOpened.set(isOpen)
    }


    override fun bindLayout(): Int = R.layout.fragment_register

}