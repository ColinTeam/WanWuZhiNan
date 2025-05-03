package com.colin.library.android.widget

import android.graphics.Color
import com.colin.library.android.utils.ext.dp

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Date  :2024-09-11
 *
 * Des   :常量
 */
interface Constants {
    companion object {

        private val dp_7 = 7F.dp()
        private val dp_5 = 5F.dp()
        private val dp_3 = 3F.dp()

        const val BANNER_LOOP_AUTO = true
        const val BANNER_LOOP_INFINITE = true
        const val BANNER_LOOP_TIME = 5000
        const val BANNER_SCROLL_TIME = 800
        const val INDICATOR_NORMAL_COLOR = Color.GRAY
        const val INDICATOR_SELECTED_COLOR = Color.BLACK

        const val SHIMMER_ANGLE = 20
        const val SHIMMER_MASK_WIDTH = 0.5F
        const val SHIMMER_GRADIENT_WIDTH = 0.1F

        val INDICATOR_NORMAL_WIDTH = dp_5
        val INDICATOR_SELECTED_WIDTH = dp_7
        val INDICATOR_SPACE = dp_5
        val INDICATOR_MARGIN = dp_5
        val INDICATOR_HEIGHT = dp_3
        val INDICATOR_RADIUS = dp_3
    }
}