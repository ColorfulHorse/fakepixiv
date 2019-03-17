package com.lyj.fakepivix.module.login

import android.app.Dialog
import android.app.TimePickerDialog
import android.support.v7.app.AlertDialog
import android.widget.TimePicker
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.BaseActivity
import com.lyj.fakepivix.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    override var mViewModel: LoginViewModel = LoginViewModel()

    override fun initData() {
        mBinding.vm = mViewModel
    }

    override fun initView() {
        TimePicker(this)
                .setOnTimeChangedListener(TimePicker.OnTimeChangedListener())
    }

    override fun bindLayout(): Int = R.layout.activity_login


}
