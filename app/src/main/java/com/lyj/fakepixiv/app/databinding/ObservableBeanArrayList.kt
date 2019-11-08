package com.lyj.fakepixiv.app.databinding

import androidx.databinding.BaseObservable
import androidx.databinding.ListChangeRegistry
import androidx.databinding.Observable
import androidx.databinding.ObservableList
import java.util.ArrayList

/**
 * @author greensun
 *
 * @date 2019/9/18
 *
 * @desc
 */
class ObservableBeanArrayList <T : BaseObservable> : ArrayList<T>(), ObservableList<T> {

    @Transient
    private var mListeners: ListChangeRegistry = ListChangeRegistry()


    override fun addOnListChangedCallback(callback: ObservableList.OnListChangedCallback<out ObservableList<T>>?) {
        this.mListeners.add(callback)
    }

    override fun removeOnListChangedCallback(callback: ObservableList.OnListChangedCallback<out ObservableList<T>>?) {
        this.mListeners.remove(callback)
    }


    override fun add(element : T): Boolean {
        super.add(element)
        this.notifyAdd(this.size - 1, 1)
        element.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                notifyChanged(element)
            }
        })
        return true
    }

    override fun add(index: Int, element : T) {
        super.add(index, element)
        this.notifyAdd(index, 1)
        element.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                notifyChanged(element)
            }
        })
    }

    override fun addAll(elements: Collection<T>): Boolean {
        val oldSize = this.size
        val added = super.addAll(elements)
        if (added) {
            this.notifyAdd(oldSize, this.size - oldSize)
        }
        elements.forEach {
            it.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
                override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                    notifyChanged(it)
                }
            })
        }
        return added
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        val added = super.addAll(index, elements)
        if (added) {
            this.notifyAdd(index, elements.size)
        }
        elements.forEach {
            it.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
                override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                    notifyChanged(it)
                }
            })
        }
        return added
    }

    override fun clear() {
        val oldSize = this.size
        super.clear()
        if (oldSize != 0) {
            this.notifyRemove(0, oldSize)
        }

    }

    override fun removeAt(index: Int): T {
        val element = super.removeAt(index)
        this.notifyRemove(index, 1)
        return element
    }

    override fun remove(element: T): Boolean {
        val index = this.indexOf(element)
        return if (index >= 0) {
            this.removeAt(index)
            true
        } else {
            false
        }
    }

    override fun set(index: Int, element: T): T {
        super.set(index, element)
        this.mListeners.notifyChanged(this, index, 1)
        return element
    }


    override fun removeRange(fromIndex: Int, toIndex: Int) {
        super.removeRange(fromIndex, toIndex)
        this.notifyRemove(fromIndex, toIndex - fromIndex)
    }

    fun notifyAdd(start: Int, count: Int) {
        this.mListeners.notifyInserted(this, start, count)
    }

    fun notifyRemove(start: Int, count: Int) {
        this.mListeners.notifyRemoved(this, start, count)
    }

    fun notifyChanged(element: T) {
        this.mListeners.notifyChanged(this)
       // this.mListeners.notifyChanged(this, indexOf(element), 1)
    }

}