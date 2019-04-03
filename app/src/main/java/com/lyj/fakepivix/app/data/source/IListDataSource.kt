package com.lyj.fakepivix.app.data.source

import android.util.LruCache
import io.reactivex.Observable

/**
 * @author greensun
 *
 * @date 2019/4/3
 *
 * @desc
 */
interface IListDataSource<T> {

    fun getData(): Observable<MutableList<T>>

    fun deleteAllData()
}
