package com.colin.library.android.utils.ext

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-01 03:39
 *
 * Des   :DimensExt
 */
import android.util.TypedValue
import com.colin.library.android.utils.ResourcesUtil


/**
 * 将 float 值转换为 dp
 */
fun Float.dp() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, this, ResourcesUtil.getResources().displayMetrics
)


/**
 * 将 int 值转换为 dp
 */
fun Int.dp() = this.toFloat().dp().toInt()

/**
 * 将 float 值转换为 px
 */
fun Float.px() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_PX, this, ResourcesUtil.getResources().displayMetrics
)

/**
 * 将 int 值转换为 px
 */
fun Int.px() = this.toFloat().dp().toInt()

/**
 * 将 float 值转换为 sp
 */
fun Float.sp() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP, this, ResourcesUtil.getResources().displayMetrics
)


/**
 * 将 int 值转换为 sp
 */
fun Int.sp() = this.toFloat().sp().toInt()

