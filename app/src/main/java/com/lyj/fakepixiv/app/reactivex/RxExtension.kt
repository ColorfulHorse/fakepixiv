package com.lyj.fakepixiv.app.reactivex

import com.lyj.fakepixiv.app.data.source.remote.UserRepository
import com.lyj.fakepixiv.app.network.ApiException
import com.lyj.fakepixiv.app.utils.SPUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author greensun
 *
 * @date 2019/4/3
 *
 * @desc
 */
fun <T> Observable<T>.io(): Observable<T> = this.compose {
    it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

/**
 * 用于刷新token
 */

//@Deprecated("更改为ApiExceptionInterceptor实现")
//fun <T> Observable<T>.retryWhenTokenInvalid(): Observable<T> = this.retryWhen {
//    it.flatMap { err ->
//        if (err is ApiException) {
//            if (err.code == ApiException.CODE_TOKEN_INVALID) {
//                val loginData = SPUtil.getLoginData()
//                loginData?.let {
//                    return@flatMap UserRepository.instance.reLogin(loginData)
//                }
//            }
//        }
//        return@flatMap Observable.error<T>(err)
//    }
//}