package com.lyj.fakepivix.app.base;

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * @author greensun
 * @date 2019/3/16
 * @desc
 */
open class BaseModel : IModel {

    private var mDisposable: CompositeDisposable? = null

    protected fun addDidposable(disposable: Disposable) {
        mDisposable?.let {
            mDisposable = CompositeDisposable()
        }
        mDisposable?.add(disposable)
    }

    override fun destroy() {
        mDisposable?.let {
            it.dispose()
        }
    }
}
