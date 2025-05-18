package com.ssm.comm.ext

import android.content.Intent
import android.content.pm.PackageInfo
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.ssm.comm.app.appContext
import com.ssm.comm.config.Constant
import com.ssm.comm.data.VersionData
import com.ssm.comm.utils.LogUtils
import java.io.File
import java.io.IOException
import java.math.BigDecimal

@Suppress("DEPRECATION")
fun getCurrentVersionCode(): Long {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        getCurrentPackageInfo().longVersionCode
    } else {
        getCurrentPackageInfo().versionCode.toLong()
    }
}

fun getCurrentVersionName(): String {
    return "${getCurrentPackageInfo().versionName}"
}

private fun getCurrentPackageInfo(): PackageInfo {
    return appContext.packageManager.getPackageInfo(getCurrentPackageName(), 0)
}

fun getCurrentPackageName(): String {
    return appContext.packageName
}


fun installApk(file: File) {
    setPermission()
    if (file.name.endsWith(".apk")) {
        val intent = Intent()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.action = Intent.ACTION_VIEW
        val type = "application/vnd.android.package-archive"
        // 适配7.0安装
        if (Build.VERSION.SDK_INT > 24) {
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数 3共享的文件
            val authority = getCurrentPackageName() + ".fileProvider"
            LogUtils.e("authority============>${authority}")
            val uri = FileProvider.getUriForFile(appContext, authority, file)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(uri, type)
        } else {
            intent.setDataAndType(Uri.fromFile(file), type)
        }
        appContext.startActivity(intent)
    }
}


/**
 * 提升读写权限
 * @throws //否则在安装的时候会出现apk解析失败的页面
 */
private fun setPermission() {
    val command = "chmod " + "777" + " " + Constant.APK_PATH
    val runtime = Runtime.getRuntime()
    try {
        runtime.exec(command)
    } catch (e: IOException) {
        e.printStackTrace()
    }
}


/**
 * 保存到本地
 */
fun copyApkFromAssets() {
    try {

    } catch (e: IOException) {
        e.printStackTrace()
    }
}


