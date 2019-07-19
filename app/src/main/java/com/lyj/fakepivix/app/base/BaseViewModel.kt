package com.lyj.fakepivix.app.base;

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.databinding.BaseObservable
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import org.jetbrains.annotations.NotNull


/**
 * @author greensun
 *
 * @date 2019/3/16
 *
 * @desc
 */
abstract class BaseViewModel<M : IModel?> : BaseObservable(), LifecycleObserver, CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private val mDisposable: CompositeDisposable by lazy { CompositeDisposable() }


    protected abstract val mModel: M


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate(@NotNull owner: LifecycleOwner) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy(@NotNull owner: LifecycleOwner) {
        mModel?.destroy()
        mDisposable.dispose()
        cancel()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    open fun onLifecycleChanged(@NotNull owner: LifecycleOwner, @NotNull event: Lifecycle.Event) {

    }

    protected fun addDisposable(disposable: Disposable) {
        mDisposable.add(disposable)
    }
}
