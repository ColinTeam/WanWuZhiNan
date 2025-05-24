package com.colin.library.android.widget.banner.def

import androidx.annotation.IntDef
import androidx.recyclerview.widget.RecyclerView

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-24 12:09
 *
 * Des   :BannerIndicatorDef
 */

@IntDef(IndicatorGravity.CENTER, IndicatorGravity.START, IndicatorGravity.END)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD)
annotation class IndicatorGravity {
    companion object {
        const val CENTER = 0
        const val START = 1 shl 1
        const val END = 1 shl 2
    }
}


@IntDef(
    IndicatorOrientation.INDICATOR_HORIZONTAL,
    IndicatorOrientation.INDICATOR_VERTICAL,
    IndicatorOrientation.INDICATOR_RTL
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD)
annotation class IndicatorOrientation {
    companion object {
        const val INDICATOR_HORIZONTAL = RecyclerView.HORIZONTAL
        const val INDICATOR_VERTICAL = RecyclerView.VERTICAL
        const val INDICATOR_RTL = 3
    }
}

@IntDef(
    IndicatorSlideMode.NORMAL,
    IndicatorSlideMode.SMOOTH,
    IndicatorSlideMode.WORM,
    IndicatorSlideMode.COLOR,
    IndicatorSlideMode.SCALE
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD)
annotation class IndicatorSlideMode {
    companion object {
        const val NORMAL = 0
        const val SMOOTH = 2
        const val WORM = 3
        const val SCALE = 4
        const val COLOR = 5
    }
}


@IntDef(IndicatorStyle.CIRCLE, IndicatorStyle.DASH, IndicatorStyle.ROUND_RECT)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD)
annotation class IndicatorStyle {
    companion object {
        const val CIRCLE = 0
        const val DASH = 1 shl 1
        const val ROUND_RECT = 1 shl 2
    }
}
