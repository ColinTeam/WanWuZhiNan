package com.comm.net_work.sign

import android.text.TextUtils
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.net_work.sign
 * ClassName: Md5Utils
 * Author:ShiMing Shi
 * CreateDate: 2022/9/19 14:18
 * Email:shiming024@163.com
 * Description:
 */
object Md5Utils {

    fun md5(str: String): String{
        if(TextUtils.isEmpty(str)){
            return ""
        }
        val md5 = MessageDigest.getInstance("MD5")
        try {
            val bytes = md5.digest(str.toByteArray())
            val result = StringBuilder()
            for (b in bytes) {
                var temp = Integer.toHexString(b.toInt() and 0xff)
                if (temp.length == 1) {
                    temp = "0$temp"
                }
                result.append(temp)
            }
            return result.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }
}