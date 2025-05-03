package com.colin.library.android.utils

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-01 02:59
 *
 * Des   :VibratorUtil
 */
object VibratorUtil {

    @RequiresPermission(Manifest.permission.VIBRATE)
    fun startVibrator(context: Context) {
        val effect = VibrationEffect.createOneShot(100, 30)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.getSystemService<VibratorManager>(context, VibratorManager::class.java)
                ?.vibrate(CombinedVibration.createParallel(effect))
        } else {
            ContextCompat.getSystemService<Vibrator>(context, Vibrator::class.java)?.vibrate(effect)
        }

    }
}