package com.lyj.fakepivix.app.constant;

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

@StringDef(value = {Restrict.ALL, Restrict.PUBLIC, Restrict.PRIVATE})
@Target(value = {ElementType.PARAMETER})
@Retention(RetentionPolicy.SOURCE)
public @interface Restrict {
    String ALL = "all";
    String PUBLIC = "public";
    String PRIVATE = "private";
}
