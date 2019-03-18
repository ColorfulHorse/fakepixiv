package com.lyj.fakepivix.module.login

import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.BaseActivity
import com.lyj.fakepivix.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    override var mViewModel: LoginViewModel = LoginViewModel()

    override fun initData() {
        mBinding.vm = mViewModel
    }

    override fun initView() {

    }

    override fun bindLayout(): Int = R.layout.activity_login


}
