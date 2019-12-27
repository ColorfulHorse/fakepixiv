package com.lyj.fakepixiv.app.delegates

import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

/**
 * @author green sun
 *
 * @date 2019/12/23
 *
 * @desc 弱引用委托
 */
class Weak<T : Any>(initializer: () -> T?) {
    var weakReference = WeakReference(initializer())

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return weakReference.get()
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        weakReference = WeakReference(value)
    }

}