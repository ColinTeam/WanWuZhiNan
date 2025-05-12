package com.colin.library.android.utils.encrypt

import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
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

        @JvmStatic
        fun aes(data: String, key: String): String? {
            try {
                // Base64 解码
                val decodedData = Base64.decode(data, Base64.DEFAULT)
                // 提取 IV 和密文
                val iv = decodedData.copyOfRange(0, 16)
                val ciphertext = decodedData.copyOfRange(16, decodedData.size)
                // 生成 AES-128 密钥
                val sha256 = MessageDigest.getInstance("SHA-256")
                val hashKey = sha256.digest(key.toByteArray())
                val aesKey = hashKey.copyOfRange(0, 16) // 取前16字节
                // 创建 SecretKeySpec 和 IvParameterSpec
                val secretKeySpec = SecretKeySpec(aesKey, "AES")
                val ivParameterSpec = IvParameterSpec(iv)
                // 创建 Cipher 实例
                val cipher = Cipher.getInstance(TRANSFORMATION_AES)
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)
                // 解密数据
                val decryptedData = cipher.doFinal(ciphertext)
                // 转换为字符串返回
                return String(decryptedData)
            } catch (e: Exception) {
                e.printStackTrace()
                return null // 解密失败
            }
        }
    }


}