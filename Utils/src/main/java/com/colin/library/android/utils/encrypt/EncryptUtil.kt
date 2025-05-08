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
            return if (value.isNullOrEmpty()) "" else md5(value.toByteArray(StandardCharsets.UTF_8))
        }

        @JvmStatic
        fun md5(bytes: ByteArray): String {
            return try {
                val digest = MessageDigest.getInstance("MD5").digest(bytes)
                digest.joinToString(separator = "") { "%02x".format(it) }
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
                ""
            }
        }


        @JvmStatic
        fun aes(
            value: String?, key: String, iv: String, transformation: String = TRANSFORMATION_AES
        ): String {
            return if (value.isNullOrEmpty()) ""
            else aes(value.toByteArray(StandardCharsets.UTF_8), key, iv, transformation)
        }

        @JvmStatic
        fun aes(
            bytes: ByteArray, key: String, iv: String, transformation: String = TRANSFORMATION_AES
        ): String {
            return try {
                val cipher = Cipher.getInstance(transformation)
                val rawKey = key.toByteArray(StandardCharsets.UTF_8)
                val rawIV = iv.toByteArray(StandardCharsets.UTF_8)
                val secretKey = SecretKeySpec(rawKey, "AES")
                val ivParam = IvParameterSpec(rawIV)
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParam)
                val encrypted = cipher.doFinal(bytes)
                Base64.encodeToString(encrypted, Base64.DEFAULT)
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
    }
}