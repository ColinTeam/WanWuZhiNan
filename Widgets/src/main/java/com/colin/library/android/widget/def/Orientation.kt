package com.colin.library.android.widget.def

import android.widget.LinearLayout
import androidx.annotation.IntDef

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025/5/1 08:46
 *
 * Des   :Orientation
 */
@IntDef(Orientation.HORIZONTAL, Orientation.VERTICAL)
@Retention(AnnotationRetention.SOURCE)
annotation class Orientation {
    companion object {
        const val HORIZONTAL = LinearLayout.HORIZONTAL
        const val VERTICAL = LinearLayout.VERTICAL
    }
}

