package com.lyj.fakepixiv.app.databinding

import androidx.databinding.BaseObservable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author green sun
 *
 * @date 2019/12/17
 *
 * @desc
 */
class Dynamic<T>(var value: T, val id: Int) : ReadWriteProperty<BaseObservable?, T> {

    override fun getValue(thisRef: BaseObservable?, property: KProperty<*>): T {
        return value
    }

    override fun setValue(thisRef: BaseObservable?, property: KProperty<*>, value: T) {
        this.value = value
        thisRef?.let {
            it.notifyPropertyChanged(id)
        }
    }
}