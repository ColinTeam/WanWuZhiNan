package com.wanwuzhinan.mingchang.utils

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.text.Editable
import android.view.WindowManager
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.colin.library.android.image.glide.GlideImgManager
import com.colin.library.android.network.NetworkConfig
import com.ssm.comm.config.Constant
import com.ssm.comm.ext.getCurrentVersionCode
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.entity.ConfigData
import java.util.regex.Pattern

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-03 19:53
 *
 * Des   :AppExt
 */
const val RATIO_16_9 = 16.0F / 9.0F
const val RATIO_4_3 = 4.0F / 3.0F
const val ANDROID_ASSET_URI = "file:///android_asset/"

fun ImageView.load(url: String, res: Int = R.mipmap.default_icon) {
    GlideImgManager.get().loadImg(url, this, res)
}

fun Fragment.navigate(action: Int): NavController {
    return findNavController().apply {
        this.navigate(action)
    }
}

@Suppress("DEPRECATION")
fun Context.getRatio(): Float {
    val point = Point()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        this.display.getSize(point)
    } else {
        getSystemService<WindowManager>(WindowManager::class.java).defaultDisplay.getSize(point)
    }
    return point.x.toFloat() / point.y.toFloat()
}

fun Editable?.removeChinese(): Editable? {
    if (this.isNullOrEmpty()) return this
    val regex = "[\\u4e00-\\u9fa5]" // 匹配中文字符的正则表达式
    val pattern = Pattern.compile(regex)
    val matcher = pattern.matcher(this)
    if (matcher.find()) {
        // 如果包含中文字符，删除中文字符
        this.replace(0, this.length, this.toString().replace(regex.toRegex(), ""))
    }
    return this
}

fun Context.updateConfig(data: ConfigData) {
    //不是华为，或者发布的版本大于安装的版本 不需要审核（0）
    if (!isHuawei() || data.android_code > getCurrentVersionCode()) {
        data.apple_is_audit = 0
    }
    //全局实用
    setData(Constant.CONFIG_DATA, NetworkConfig.gson.toJson(data))
}

fun isHuawei() = ("huawei".equals(Build.BRAND, true) || "huawei".equals(
    Build.MANUFACTURER, true
) || "honor".equals(Build.BRAND, true) || "honor".equals(Build.MANUFACTURER, true))


