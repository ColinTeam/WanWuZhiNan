package com.colin.library.android.utils

import android.content.Context
import android.content.res.ColorStateList
import androidx.annotation.ArrayRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import com.colin.library.android.utils.helper.UtilHelper

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-01 03:45
 *
 * Des   :ResourcesUtil
 */
object ResourcesUtil {
    fun getResources() = getResources(UtilHelper.getApplication())
    fun getResources(context: Context) = context.resources

    @ColorInt
    fun getColor(context: Context, @ColorRes res: Int): Int {
        return ResourcesCompat.getColor(context.resources, res, context.theme)
    }

    fun getColorList(context: Context, @ColorRes res: Int): ColorStateList {
        return ColorStateList.valueOf(getColor(context, res))
    }

    @ColorInt
    fun getIntArray(context: Context, @ArrayRes res: Int): IntArray {
        return context.resources.getIntArray(res)
    }

    fun getString(@StringRes res: Int): String {
        return getString(UtilHelper.getApplication(), res)
    }

    fun getString(context: Context, @StringRes res: Int): String {
        return context.resources.getString(res)
    }
}
