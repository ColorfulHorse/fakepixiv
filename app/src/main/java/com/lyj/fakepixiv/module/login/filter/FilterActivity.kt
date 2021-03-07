package com.lyj.fakepixiv.module.login.filter

import android.content.Intent
import android.os.Bundle
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.BaseActivity
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.app.utils.Router
import com.lyj.fakepixiv.app.utils.doOnPropertyChanged
import com.lyj.fakepixiv.databinding.ActivityFilterBinding
import com.lyj.fakepixiv.module.main.MainActivity
import java.security.SecureRandom

/**
 * @author greensun
 *
 * @date 2021/2/27
 *
 * @desc 拦截路由
 */
class FilterActivity : BaseActivity<ActivityFilterBinding, FilterViewModel>() {

    override val mViewModel: FilterViewModel by lazy { FilterViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.data?.apply {
            // pixiv://account/login?code=S_AG4yElTeFHc7KhKBkzvo6k6vqu1MiMV8m7AjPjYLg&via=login
            when (path) {
                "/login" -> {
                    val code = getQueryParameter("code") ?: ""
                    val via = getQueryParameter("via") ?: ""
                    mViewModel.login(code, via)
                }
            }
        }
        mViewModel.loginState.doOnPropertyChanged(this.lifecycle) { _, _ ->
            if (mViewModel.loginState.get() is LoadState.Succeed) {
                val intent = Intent(this, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                startActivity(intent)
                finish()
            }
        }
    }

    override fun bindLayout(): Int = R.layout.activity_filter
}