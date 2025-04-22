package com.comm.net_work.sign

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import java.security.MessageDigest

object AESDecryptor {

    fun decryptAES(encryptedData: String, key: String): String? {
        try {
            // Base64 解码
            val decodedData = Base64.decode(encryptedData, Base64.DEFAULT)

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
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
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