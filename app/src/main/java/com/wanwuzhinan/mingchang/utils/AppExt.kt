package com.wanwuzhinan.mingchang.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Build
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.wanwuzhinan.mingchang.ui.pad.AudioHomeIpadActivity
import com.wanwuzhinan.mingchang.ui.phone.AudioHomeActivity

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-03 19:53
 *
 * Des   :AppExt
 */
const val RATIO_16_9 = 16.0F / 9.0F
const val RATIO_4_3 = 4.0F / 3.0F
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

fun startAudioActivity(activity: Activity, radio: Float = activity.getRatio()) {
    if (radio > RATIO_16_9) start(activity, AudioHomeActivity::class.java)
    else start(activity, AudioHomeIpadActivity::class.java)
}

inline fun <reified T : AppCompatActivity> Fragment.start(clazz: Class<T>) {
    activity?.let { it.startActivity(Intent(it, clazz)) }
}

inline fun <reified T : AppCompatActivity> Activity.start(clazz: Class<T>) {
    this.startActivity(Intent(this, clazz))
}

inline fun <reified T : AppCompatActivity> start(activity: Activity, clazz: Class<T>) {
    activity.startActivity(Intent(activity, clazz))
}