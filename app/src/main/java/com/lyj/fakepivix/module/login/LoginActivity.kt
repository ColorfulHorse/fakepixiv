package com.lyj.fakepivix.module.login

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.lyj.fakepivix.R
import com.lyj.fakepivix.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        mBinding.vm = LoginViewModel().apply {
            userName = "lyj"
            password = "132165464"
        }
    }
}
