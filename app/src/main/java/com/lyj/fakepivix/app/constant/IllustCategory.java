package com.lyj.fakepivix.app.constant;

import android.support.annotation.StringDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.AnnotationTarget;

/**
 * @author greensun
 * @date 2019/5/27
 * @desc
 */

@StringDef(value = {IllustCategory.ILLUST, IllustCategory.COMIC, IllustCategory.NOVEL, IllustCategory.ILLUSTANDCOMIC})
@Target(value = {ElementType.PARAMETER})
@Retention(RetentionPolicy.SOURCE)
public @interface IllustCategory {
    String ILLUST = "illust";
    String COMIC = "manga";
    String NOVEL = "novel";
    // 插画·漫画
    String ILLUSTANDCOMIC = "ILLUSTANDCOMIC";
}
