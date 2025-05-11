package com.wanwuzhinan.mingchang.utils

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.WindowManager
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.colin.library.android.image.glide.GlideImgManager
import com.wanwuzhinan.mingchang.R

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


