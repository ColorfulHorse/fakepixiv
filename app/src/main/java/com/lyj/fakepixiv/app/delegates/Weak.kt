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
class Weak<T>(source: T?) {
    private var weakReference = WeakReference(source)

    operator fun getValue(thisRef: T?, property: KProperty<*>): T? {
        return weakReference.get()
    }

    operator fun setValue(thisRef: T?, property: KProperty<*>, value: T?) {
        weakReference = WeakReference(value)
    }
}

fun <T> weak(initializer: () -> T?) = Weak(initializer())