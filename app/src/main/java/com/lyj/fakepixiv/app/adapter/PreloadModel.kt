package com.lyj.fakepixiv.app.adapter

/**
 * @author greensun
 *
 * @date 2019/8/6
 *
 * @desc 预加载模型
 */
interface PreloadModel {
    fun getPreloadUrls(): List<String>
}