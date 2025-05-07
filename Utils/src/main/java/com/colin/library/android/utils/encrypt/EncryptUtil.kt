package com.colin.library.android.utils.encrypt

import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-07 13:34
 *
 * Des   :EncryptUtil 加密工具类
 */
class EncryptUtil {
    companion object {
        @JvmStatic
        fun md5(value: String?): String {
            return if (value.isNullOrEmpty()) "" else md5(value.toByteArray())
        }

        @JvmStatic
        fun md5(bytes: ByteArray): String {
            try {
                val bytes = MessageDigest.getInstance("MD5").digest(bytes)
                val result = StringBuilder()
                for (b in bytes) {
                    var hex = Integer.toHexString(b.toInt() and 0xff)
                    if (hex.length == 1) hex = "0$hex"
                    result.append(hex)
                }
                return result.toString()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
            return ""
        }


        @JvmStatic
        fun aes(value: String?, transformation: String, key: String, iv: String): String {
            return if (value.isNullOrEmpty()) ""
            else aes(value.toByteArray(StandardCharsets.UTF_8), transformation, key, iv)
        }

        @JvmStatic
        fun aes(value: ByteArray, transformation: String, key: String, iv: String): String {
            try {
                val cipher = Cipher.getInstance(transformation)
                val raw: ByteArray = key.toByteArray()
                val spec = SecretKeySpec(raw, "AES")
                val iv = IvParameterSpec(iv.toByteArray())
                cipher.init(Cipher.ENCRYPT_MODE, spec, iv)
                val encrypted = cipher.doFinal(value)
                return String(Base64.encode(encrypted, Base64.DEFAULT))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }
    }
}