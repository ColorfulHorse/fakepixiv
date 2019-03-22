package com.lyj.fakepivix.app.base;

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.databinding.BaseObservable
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.jetbrains.annotations.NotNull


/**
 * @author greensun
 * @date 2019/3/16
 * @desc
 */
abstract class BaseViewModel<M : IModel> : BaseObservable(), LifecycleObserver {
    private var mDisposable: CompositeDisposable? = null


    abstract var mModel: M


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate(@NotNull owner: LifecycleOwner) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy(@NotNull owner: LifecycleOwner) {
        mModel.destroy()
        mDisposable?.dispose()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    open fun onLifecycleChanged(@NotNull owner: LifecycleOwner, @NotNull event: Lifecycle.Event) {

    }

    protected fun addDisposable(disposable: Disposable) {
        mDisposable?.let {
            mDisposable = CompositeDisposable()
        }
        mDisposable?.add(disposable)
    }


}
