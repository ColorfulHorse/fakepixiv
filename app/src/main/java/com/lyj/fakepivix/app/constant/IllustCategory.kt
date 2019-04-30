package com.lyj.fakepivix.app.constant

import android.support.annotation.StringDef

/**
 * @author greensun
 *
 * @date 2019/4/28
 *
 * @desc illust分类
 */

const val ILLUST = "illust"
const val COMIC = "manga"
const val NOVEL = "novel"

@StringDef(ILLUST, COMIC, NOVEL)
@MustBeDocumented
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class IllustCategory

