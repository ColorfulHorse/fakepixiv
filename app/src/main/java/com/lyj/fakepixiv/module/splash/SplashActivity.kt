package com.lyj.fakepixiv.module.splash

import android.os.Bundle
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.base.FragmentationActivity
import com.lyj.fakepixiv.app.data.source.remote.UserRepository
import com.lyj.fakepixiv.app.reactivex.schedulerTransform
import com.lyj.fakepixiv.app.utils.SPUtil
import com.lyj.fakepixiv.app.utils.startActivity
import com.lyj.fakepixiv.databinding.ActivitySplashBinding
import com.lyj.fakepixiv.module.main.MainActivity
import com.lyj.fakepixiv.module.login.LoginActivity
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
                    .schedulerTransform()
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