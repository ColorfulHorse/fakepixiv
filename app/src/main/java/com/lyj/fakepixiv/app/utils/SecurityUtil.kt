package com.lyj.fakepixiv.app.utils

import android.os.UserManager
import android.util.Base64
import com.lyj.fakepixiv.app.constant.Constant
import com.lyj.fakepixiv.app.data.source.remote.UserRepository
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.SecureRandom

/**
 * @author greensun
 *
 * @date 2021/3/6
 *
 * @desc
 */
object SecurityUtil {
    val random = SecureRandom()

    val sha256 = MessageDigest.getInstance("SHA-256")

    // 登录校验
    fun getCodeVerifier(): String {
        val byteArray = ByteArray(32)
        random.nextBytes(byteArray)
        val encodeToString = Base64.encodeToString(byteArray, 11)
        UserRepository.instance.code_verifier = encodeToString
        val bytes = encodeToString.toByteArray(Charset.forName("US-ASCII"))
        sha256.update(bytes)
        return Base64.encodeToString(sha256.digest(), 11)
    }

    fun test(): String {
        val encodeToString = "XrfNDZ64X7jCRFrg9i5cDgzdUX2WXzifhLgX67hLVV4"
        val bytes = encodeToString.toByteArray(Charset.forName("US-ASCII"))
        sha256.update(bytes)
        return Base64.encodeToString(sha256.digest(), 11)
    }
}