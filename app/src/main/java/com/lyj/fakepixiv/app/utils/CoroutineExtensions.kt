package com.lyj.fakepixiv.app.utils

import androidx.databinding.ObservableField
import com.lyj.fakepixiv.app.network.LoadState
import kotlinx.coroutines.*

/**
 * @author green sun
 *
 * @date 2019/11/20
 *
 * @desc
 */

/**
 * 简单封装请求网络
 */
fun <T> CoroutineScope.ioTask(loadState: ObservableField<LoadState>? = null, thenAsync: ((T) -> Unit)? = null, then: ((T) -> Unit)? = null, task: suspend () -> T) {
    launch(CoroutineExceptionHandler { _, err ->
        loadState?.set(LoadState.Failed(err))
    }) {
        loadState?.set(LoadState.Loading)
        val res = withContext(Dispatchers.IO) {
            val result = task()
            thenAsync?.invoke(result)
            result
        }
        then?.invoke(res)
        loadState?.set(LoadState.Succeed)
    }
}