package com.lyj.fakepivix.module.sample

import java.util.regex.Pattern

/**
 * @author greensun
 *
 * @date 2019/7/25
 *
 * @desc
 */

fun main() {
    val str = "rew  erw erwewr   sd"
    val res = str.split(Pattern.compile("\\s+"))
    println(res.toString())
}