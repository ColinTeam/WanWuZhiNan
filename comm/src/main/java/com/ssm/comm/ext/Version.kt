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

fun getCurrentVersionCode(): Int {
    return getCurrentPackageInfo().versionCode
}

fun getCurrentVersionName(): String {
    return getCurrentPackageInfo().versionName
}

private fun getCurrentPackageInfo(): PackageInfo {
    return appContext.packageManager.getPackageInfo(getCurrentPackageName(), 0)
}

fun getCurrentPackageName(): String {
    return appContext.packageName
}

fun isNeedUpdate(data: VersionData): Boolean {
    val android = data.android
    //获得最新 versionCode
    var newVersion = data.android.version_code
    newVersion = newVersion.filterNot { it == "."[0] }
    var curVersion = getCurrentVersionName()
    curVersion = curVersion.filterNot { it == "."[0] }
    LogUtils.e("newVersion====================>$newVersion")
    LogUtils.e("curVersion====================>$curVersion")
    if(isEmpty(newVersion) || isEmpty(curVersion)){
        return false
    }
    val b1 = BigDecimal(newVersion)
    val b2 = BigDecimal(curVersion)
    return b1 > b2
}

//0代表相等，1代表version1大于version2，-1代表version1小于version2
private fun compareVersion(newVersion: String, curVersion: String): Int {
    if (isEqualStr(newVersion, curVersion)) {
        return 0
    }
    val v1Array = newVersion.split("\\.")
    val v2Array = curVersion.split("\\.")
    var index = 0
    // 获取最小长度值
    val minLen: Int = v1Array.size.coerceAtMost(v2Array.size)
    var diff = 0
    // 循环判断每位的大小
    while (index < minLen
        && v1Array[index].toInt() - v2Array.get(index).toInt()
            .also { diff = it } == 0
    ) {
        index++
    }
    if (diff == 0) {
        // 如果位数不一致，比较多余位数
        for (i in index until v1Array.size) {
            if (v1Array[i].toInt() > 0) {
                return 1
            }
        }
        for (i in index until v2Array.size) {
            if (v2Array[i].toInt() > 0) {
                return -1
            }
        }
        return 0
    }
    return if (diff > 0) 1 else -1
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


