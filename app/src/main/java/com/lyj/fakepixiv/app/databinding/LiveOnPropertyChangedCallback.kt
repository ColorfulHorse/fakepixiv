package com.lyj.fakepixiv.app.databinding

import androidx.databinding.BaseObservable
import androidx.databinding.Observable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import org.jetbrains.annotations.NotNull

/**
 * @author green sun
 *
 * @date 2019/12/24
 *
 * @desc 共用viewModel 监听同一个BaseObservable 导致内存泄漏
 */
class LiveOnPropertyChangedCallback(val observable: BaseObservable,
                                    val lifecycle: Lifecycle?,
                                    val action: (Observable, Int) -> Unit)
    : Observable.OnPropertyChangedCallback(), LifecycleObserver {

    init {
        lifecycle?.addObserver(this)
    }

    override fun onPropertyChanged(sender: Observable, propertyId: Int) {
        action(sender, propertyId)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(@NotNull owner: LifecycleOwner) {
        observable.removeOnPropertyChangedCallback(this)
    }

}