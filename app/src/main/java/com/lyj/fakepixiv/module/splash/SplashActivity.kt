package com.lyj.fakepixiv.module.splash

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.base.FragmentationActivity
import com.lyj.fakepixiv.app.constant.Constant
import com.lyj.fakepixiv.app.data.source.remote.UserRepository
import com.lyj.fakepixiv.app.network.ApiException
import com.lyj.fakepixiv.app.reactivex.schedulerTransform
import com.lyj.fakepixiv.app.utils.SPUtil
import com.lyj.fakepixiv.app.utils.ToastUtil
import com.lyj.fakepixiv.app.utils.startActivity
import com.lyj.fakepixiv.databinding.ActivitySplashBinding
import com.lyj.fakepixiv.module.main.MainActivity
import com.lyj.fakepixiv.module.login.LoginActivity
import com.tencent.tinker.lib.tinker.TinkerInstaller
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.*
import permissions.dispatcher.*
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * @author greensun
 *
 * @date 2019/4/12
 *
 * @desc
 */

@RuntimePermissions
class SplashActivity : FragmentationActivity<ActivitySplashBinding, BaseViewModel?>() {

    override val mViewModel: BaseViewModel? = null
    private var loginJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    fun setup() {
        val cacheData = SPUtil.getLoginData()
        if (cacheData == null) {
            startActivity(LoginActivity::class.java)
            finish()
        } else {
            UserRepository.instance.loginData = cacheData
            loginJob = GlobalScope.launch(Dispatchers.Main) {
                try {
                    withTimeout(10*1000) {
                        UserRepository.instance.reLogin(cacheData)
                    }
                    startActivity(MainActivity::class.java)
                    finish()
                }catch (ex: Exception) {
                    startActivity(MainActivity::class.java)
                    finish()
                }
            }
        }
        // 测试热更新用
        val patch = File(Constant.File.PATCH_PATH)
        if (patch.exists()) {
            TinkerInstaller.onReceiveUpgradePatch(applicationContext, patch.absolutePath)
        }
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    fun showRationale(request: PermissionRequest) {
        ToastUtil.showToast(R.string.no_permission)
        finish()
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    fun onDenied() {
        ToastUtil.showToast(R.string.no_permission)
        finish()
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    fun onNeverAskAgain() {
        ToastUtil.showToast(R.string.no_permission)
        finish()
    }


    override fun onDestroy() {
        super.onDestroy()
        loginJob?.cancel()
    }

    override fun bindLayout(): Int = R.layout.activity_splash
}