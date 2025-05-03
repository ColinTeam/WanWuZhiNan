package com.colin.library.android.widget.def

import android.view.Gravity
import androidx.annotation.IntDef


/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025/5/1 08:43
 *
 * Des   :Direction
 */

@IntDef(Direction.TOP, Direction.BOTTOM, Direction.LEFT, Direction.RIGHT, Direction.CENTER)
@Retention(AnnotationRetention.SOURCE)
annotation class Direction {
    companion object {
        const val TOP = Gravity.TOP
        const val BOTTOM = Gravity.BOTTOM
        const val LEFT = Gravity.LEFT
        const val RIGHT = Gravity.RIGHT
        const val CENTER = Gravity.CENTER
    }
}

