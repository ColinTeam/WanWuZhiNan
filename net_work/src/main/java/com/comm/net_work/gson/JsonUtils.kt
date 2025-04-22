package com.comm.net_work.gson

import android.app.Application
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.net_work.gson
 * ClassName: JsonUtils
 * Author:ShiMing Shi
 * CreateDate: 2022/9/4 15:39
 * Email:shiming024@163.com
 * Description:
 */
object JsonUtils {

    fun getJson(context: Application,fileName: String) : String{
        val stringBuilder = StringBuilder()
        try {
            val assetManager = context.assets
            val bf = BufferedReader(
                InputStreamReader(
                    assetManager.open(fileName)
                )
            )
            var line: String?
            while (bf.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }
}