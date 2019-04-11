package com.lyj.fakepivix.app.network


/**
 * @author greensun
 *
 * @date 2019/4/11
 *
 * @desc 网络加载状态
 */
sealed class LoadState {
    object Idle : LoadState()
    object Loading : LoadState()
    object Succeed : LoadState()
    class Failed(val error: Throwable) : LoadState()
}