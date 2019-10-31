package com.lyj.fakepixiv.module.splash

import android.Manifest
import android.os.Bundle
import android.os.Environment
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.base.FragmentationActivity
import com.lyj.fakepixiv.app.constant.Constant
import com.lyj.fakepixiv.app.data.source.remote.UserRepository
import com.lyj.fakepixiv.app.reactivex.schedulerTransform
import com.lyj.fakepixiv.app.utils.SPUtil
import com.lyj.fakepixiv.app.utils.startActivity
import com.lyj.fakepixiv.databinding.ActivitySplashBinding
import com.lyj.fakepixiv.module.main.MainActivity
import com.lyj.fakepixiv.module.login.LoginActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tencent.tinker.lib.tinker.TinkerInstaller
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import java.io.File
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
    private lateinit var permissionDisposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionDisposable = RxPermissions(this)
                .requestEachCombined(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                .subscribeBy (onNext = {
                    if (it.granted) {
                        setup()
                    }
                }, onError = {

                })
    }

    private fun setup() {
        val cacheData = SPUtil.getLoginData()
        if (cacheData == null) {
            startActivity(LoginActivity::class.java)
            finish()
        } else {
            disposable = UserRepository.instance
                    .reLogin(cacheData)
                    .timeout(10, TimeUnit.SECONDS)
                    .schedulerTransform()
                    .subscribeBy(onError = {
                        startActivity(MainActivity::class.java)
                        finish()
                    },onComplete = {
                        startActivity(MainActivity::class.java)
                        finish()
                    })
        }
        // 测试热更新用
        val patch = File(Constant.File.PATCH_PATH)
        if (patch.exists()) {
            TinkerInstaller.onReceiveUpgradePatch(applicationContext, patch.absolutePath)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
        permissionDisposable.dispose()
    }

    override fun bindLayout(): Int = R.layout.activity_splash
}