package com.colin.library.android.widget.banner.utils

import com.colin.library.android.widget.banner.options.IndicatorOptions

/**
 * 指示器工具类
 */
object IndicatorUtils {


    fun getCoordinateX(
        indicatorOptions: IndicatorOptions,
        maxDiameter: Float,
        index: Int
    ): Float {
        val normalIndicatorWidth = indicatorOptions.normalSliderWidth
        return maxDiameter / 2 + (normalIndicatorWidth + indicatorOptions.sliderGap) * index
    }

    fun getCoordinateY(maxDiameter: Float): Float {
        return maxDiameter / 2
    }
}
