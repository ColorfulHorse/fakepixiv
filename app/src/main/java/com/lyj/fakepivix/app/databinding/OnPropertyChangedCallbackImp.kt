package com.lyj.fakepivix.app.databinding

import android.databinding.Observable

/**
 * @author greensun
 *
 * @date 2019/4/10
 *
 * @desc
 */
class OnPropertyChangedCallbackImp(val consumer : (Observable, Int) -> Unit) : Observable.OnPropertyChangedCallback(){
    override fun onPropertyChanged(sender: Observable, propertyId: Int) {
        consumer(sender, propertyId)
    }
}