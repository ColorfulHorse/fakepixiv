package com.lyj.fakepixiv.module.login.filter

import android.os.Bundle
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.BaseActivity
import com.lyj.fakepixiv.databinding.ActivityFilterBinding
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
            when(path) {
                "/login" -> {
                    val code = getQueryParameter("code")
                    val via = getQueryParameter("via")
                    code?.let {
                        mViewModel.login(it)
                    }
                }
            }
        }
    }

    override fun bindLayout(): Int = R.layout.activity_filter
}