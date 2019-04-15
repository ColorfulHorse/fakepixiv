package com.lyj.fakepivix.app.data.source.remote

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc
 */
class LiveRepository private constructor() {

    companion object {
        val instance by lazy { LiveRepository() }
    }
}