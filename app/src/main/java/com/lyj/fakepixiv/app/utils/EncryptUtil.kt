package com.lyj.fakepixiv.app.utils

import java.lang.StringBuilder
import java.security.MessageDigest

/**
 * @author greensun
 *
 * @date 2019/9/5
 *
 * @desc
 */
object EncryptUtil {

    fun encodeClientHash(source: String): String {
        val str = MessageDigest.getInstance("MD5").digest(source.toByteArray())
        val sb = StringBuilder()
        for (byte in str) {
            sb.append(String.format("%02x", byte))
        }
        return sb.toString()
    }
}