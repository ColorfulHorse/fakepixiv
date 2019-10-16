package com.lyj.fakepixiv.app.constant;

import android.support.annotation.StringDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author greensun
 * @date 2019/5/27
 * @desc
 */

@StringDef(value = {IllustCategory.ILLUST, IllustCategory.COMIC, IllustCategory.NOVEL, IllustCategory.OTHER})
@Target(value = {ElementType.PARAMETER})
@Retention(RetentionPolicy.SOURCE)
public @interface IllustCategory {
    String ILLUST = "illust";
    String COMIC = "manga";
    String NOVEL = "novel";
    String OTHER = "other";
}
