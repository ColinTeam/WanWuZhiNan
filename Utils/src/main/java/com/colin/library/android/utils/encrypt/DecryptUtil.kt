package com.colin.library.android.utils.encrypt

import android.util.Base64
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-08 22:13
 *
 * Des   :DecryptUtil 解密工具类
 */
const val TRANSFORMATION_AES = "AES/CBC/PKCS5Padding"

class DecryptUtil {
    companion object {
        @JvmStatic
        fun aes(
            data: String, key: String, iv: String, transformation: String = TRANSFORMATION_AES
        ): String {
            return try {
                val cipher = Cipher.getInstance(transformation)
                val rawKey = key.toByteArray(StandardCharsets.UTF_8)
                val rawIV = iv.toByteArray(StandardCharsets.UTF_8)
                val secretKey = SecretKeySpec(rawKey, "AES")
                val ivParam = IvParameterSpec(rawIV)
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParam)
                val decoded = Base64.decode(data, Base64.DEFAULT)
                String(cipher.doFinal(decoded), StandardCharsets.UTF_8)
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
    }
}