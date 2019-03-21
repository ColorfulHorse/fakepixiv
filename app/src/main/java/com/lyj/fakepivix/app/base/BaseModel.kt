package com.lyj.fakepivix.app.base;

import com.lyj.fakepivix.app.network.ApiService
import com.lyj.fakepivix.app.network.retrofit.RetrofitManager
import com.lyj.fakepivix.app.rx.SchedulerTransformer
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * @author greensun
 * @date 2019/3/16
 * @desc
 */
open class BaseModel : IModel {

    private var mDisposable: CompositeDisposable? = null
    protected val mApi: ApiService = RetrofitManager.instance.obtainService(ApiService::class.java)

    protected fun addDidposable(disposable: Disposable) {
        mDisposable?.let {
            mDisposable = CompositeDisposable()
        }
        mDisposable?.add(disposable)
    }

    protected fun <T> applyScheduler(): SchedulerTransformer<T> {
        return SchedulerTransformer()
    }


    override fun destroy() {
        mDisposable?.let {
            it.dispose()
        }
    }
}
