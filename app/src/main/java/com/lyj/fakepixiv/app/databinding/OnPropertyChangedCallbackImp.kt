package com.lyj.fakepixiv.app.databinding

import androidx.databinding.Observable

/**
 * @author greensun
 *
 * @date 2019/4/10
 *
 * @desc
 */
//class onPropertyChangedCallback(val consumer : (Observable, Int) -> Unit) : Observable.OnPropertyChangedCallback(){
//    override fun onPropertyChanged(sender: Observable, propertyId: Int) {
//        consumer(sender, propertyId)
//    }
//}

inline fun onPropertyChangedCallback(crossinline consumer : (Observable, Int) -> Unit) = object : Observable.OnPropertyChangedCallback(){
    override fun onPropertyChanged(sender: Observable, propertyId: Int) {
        consumer(sender, propertyId)
    }
}