package com.lyj.fakepivix.module.splash

import android.os.Bundle
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.FragmentationActivity
import com.lyj.fakepivix.app.data.source.UserRepository
import com.lyj.fakepivix.app.reactivex.schedulerTransformer
import com.lyj.fakepivix.app.utils.SPUtil
import com.lyj.fakepivix.app.utils.startActivity
import com.lyj.fakepivix.databinding.ActivitySplashBinding
import com.lyj.fakepivix.module.main.MainActivity
import com.lyj.fakepivix.module.login.LoginActivity
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import java.util.concurrent.TimeUnit

/**
 * @author greensun
 *
 * @date 2019/4/12
 *
 * @desc
 */
class SplashActivity : FragmentationActivity<ActivitySplashBinding, BaseViewModel<*>?>() {

    override val mViewModel: BaseViewModel<*>? = null
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cacheData = SPUtil.getLoginData()
        if (cacheData == null) {
            startActivity(LoginActivity::class.java)
        } else {
            disposable = UserRepository.instance
                    .reLogin(cacheData)
                    .timeout(10, TimeUnit.SECONDS)
                    .schedulerTransformer()
                    .subscribeBy(onError = {
                        startActivity(MainActivity::class.java)
                    },onComplete = {
                        startActivity(MainActivity::class.java)
                    })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    override fun bindLayout(): Int = R.layout.activity_splash
}