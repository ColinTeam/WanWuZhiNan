package com.wanwuzhinan.mingchang.data

import android.os.Build
import com.ssm.comm.ext.getCurrentVersionCode
import com.ssm.comm.ext.getCurrentVersionName

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-13 19:55
 *
 * Des   :ErrorLog
 * 0->接口
 * 1->视频
 */
data class ErrorLog(
    val type: Int = 0,
    val os: String = "android",
    val os_version: Int = Build.VERSION.SDK_INT,
    val app_version: String = getCurrentVersionName(),
    val app_code: Long = getCurrentVersionCode(),
    val time: Long = System.currentTimeMillis(),
    val error: String
)