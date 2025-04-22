package com.comm.net_work.sign

import android.util.Base64
import com.comm.net_work.BuildConfig
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.net_work.sign
 * ClassName: AESUtils
 * Author:ShiMing Shi
 * CreateDate: 2022/9/19 14:15
 * Email:shiming024@163.com
 * Description:
 */
object AESUtils {

    // 加密
    fun encrypt(sSrc: String): String{
        try {
            val cipher = Cipher.getInstance(BuildConfig.APP_AES_MODEL)
            val raw: ByteArray = BuildConfig.APP_AES_KEY.toByteArray()
            val skeySpec = SecretKeySpec(raw, "AES")
            val iv = IvParameterSpec(BuildConfig.APP_AES_IV.toByteArray())
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv)
            val encrypted = cipher.doFinal(sSrc.toByteArray(StandardCharsets.UTF_8))
            return String(Base64.encode(encrypted, Base64.DEFAULT))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }


    // 解密
    fun decrypt(sSrc: String): String{
        return try {
            val raw: ByteArray = BuildConfig.APP_AES_KEY.toByteArray(StandardCharsets.US_ASCII)
            val skeySpec = SecretKeySpec(raw, "AES")
            val cipher = Cipher.getInstance(BuildConfig.APP_AES_MODEL)
            val iv = IvParameterSpec(BuildConfig.APP_AES_IV.toByteArray())
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv)
            val encrypted1 = Base64.decode(sSrc, Base64.DEFAULT)
            val original = cipher.doFinal(encrypted1)
            return String(original, StandardCharsets.UTF_8)
        } catch (ex: Exception) {
            ""
        }
    }
}