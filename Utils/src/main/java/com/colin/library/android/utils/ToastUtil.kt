package com.colin.library.android.utils

import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import androidx.annotation.StringRes
import com.colin.library.android.utils.helper.UtilHelper

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Date  :2024-10-10
 * Des   :Toast工具类
 * Toast 防抖，去重
 */
object ToastUtil {
    private var lastToastTime = 0L
    fun show(@StringRes res: Int, duration: Int = Toast.LENGTH_SHORT) {
        show(UtilHelper.getApplication(), ResourcesUtil.getString(res), duration)
    }

    fun show(text: CharSequence?, duration: Int = Toast.LENGTH_SHORT) {
        show(UtilHelper.getApplication(), text, duration)
    }

    fun show(context: Context?, text: CharSequence?, duration: Int) {
        if (context == null || TextUtils.isEmpty(text)) return
        val current = System.currentTimeMillis()
        if (current - lastToastTime < Constants.TIMEOUT_TOAST) return
        lastToastTime = current
        Toast.makeText(context, text, duration).show()
    }
}