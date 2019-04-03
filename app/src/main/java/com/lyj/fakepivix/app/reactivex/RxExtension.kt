package com.lyj.fakepivix.app.reactivex

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
fun <T> Observable<T>.schedulerTransformer(): Observable<T> = this.compose {
    it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}