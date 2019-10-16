package com.lyj.fakepixiv.app.base;

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.databinding.BaseObservable
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.*
import org.jetbrains.annotations.NotNull


/**
 * @author greensun
 *
 * @date 2019/3/16
 *
 * @desc
 */
abstract class BaseViewModel<M : IModel?> : BaseObservable(), LifecycleObserver,
        CoroutineScope by CoroutineScope(Dispatchers.Main + SupervisorJob()) {

    private val mDisposable: CompositeDisposable by lazy { CompositeDisposable() }
    /**
     * 子viewModel list
     */
    protected val mSubViewModelList by lazy { mutableListOf<BaseViewModel<*>>() }


    protected abstract val mModel: M

    var onCreated = false
    var lazyCreated = false

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate(@NotNull owner: LifecycleOwner) {
        onCreated = true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart(@NotNull owner: LifecycleOwner) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy(@NotNull owner: LifecycleOwner) {
        mSubViewModelList.forEach { it.onDestroy(owner) }
        mModel?.destroy()
        mDisposable.dispose()
        //cancel()
        coroutineContext.cancelChildren()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    open fun onLifecycleChanged(@NotNull owner: LifecycleOwner, @NotNull event: Lifecycle.Event) {

    }

    operator fun plus(vm: BaseViewModel<*>): BaseViewModel<*> {
        mSubViewModelList.add(vm)
        return this
    }

    /**
     * 自动取消
     */
    protected fun addDisposable(disposable: Disposable?) {
        disposable?.let {
            mDisposable.add(it)
        }
    }
}
