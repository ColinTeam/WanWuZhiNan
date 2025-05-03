package com.colin.library.android.utils.ext

import android.Manifest
import android.R.attr.action
import android.view.View
import androidx.annotation.RequiresPermission
import com.colin.library.android.utils.Constants
import com.colin.library.android.utils.VibratorUtil.startVibrator

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-01 02:52
 *
 * Des   :ViewExt
 */
/**
 * 设置防止重复点击事件
 * @param views 需要设置点击事件的view
 * @param interval 时间间隔 默认0.5秒
 * @param onClick 点击触发的方法
 */
@RequiresPermission(Manifest.permission.VIBRATE)
fun onClick(vararg views: View, interval: Long = Constants.Companion.TIMEOUT_CLICK, click: (View) -> Unit) {
    views.forEach {
        it.onClick(interval = interval) { view -> click.invoke(view) }
    }
}

/**
 * 防止重复点击事件 默认0.5秒内不可重复点击
 * @param interval 时间间隔 默认0.5秒
 * @param action 执行方法
 */

private var lastClickTime = Constants.Companion.INVALID.toLong()

@RequiresPermission(Manifest.permission.VIBRATE)
fun View.onClick(interval: Long = Constants.Companion.TIMEOUT_CLICK, click: (view: View) -> Unit) {
    setOnClickListener {
        val current = System.currentTimeMillis()
        if (lastClickTime != 0L && (current - lastClickTime < interval)) {
            return@setOnClickListener
        }
        startVibrator(context)
        lastClickTime = current
        click.invoke(it)
    }
}

