package com.colin.library.android.widget.banner.options

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.Px
import com.colin.library.android.utils.ext.px
import com.colin.library.android.widget.banner.def.IndicatorOrientation
import com.colin.library.android.widget.banner.def.IndicatorSlideMode
import com.colin.library.android.widget.banner.def.IndicatorStyle

/**
 * Indicator的配置参数
 */
class IndicatorOptions {

    @IndicatorOrientation
    var orientation = IndicatorOrientation.INDICATOR_HORIZONTAL

    @IndicatorStyle
    var indicatorStyle: Int = 0

    /**
     * Indicator滑动模式，目前仅支持两种
     *
     * @see IndicatorSlideMode.NORMAL
     *
     * @see IndicatorSlideMode.SMOOTH
     */
    @IndicatorSlideMode
    var slideMode: Int = 0

    /**
     * 页面size
     */
    var pageSize: Int = 0

    /**
     * 未选中时Indicator颜色
     */
    var normalSliderColor: Int = 0

    /**
     * 选中时Indicator颜色
     */
    @ColorInt
    var checkedSliderColor: Int = 0

    /**
     * Indicator间距
     */
    @Px
    var sliderGap: Float = 0f
    @Px
    var sliderHeight: Float = 0f
        get() = if (field > 0) field else normalSliderWidth / 2
    @Px
    var normalSliderWidth: Float = 0f
    @Px
    var checkedSliderWidth: Float = 0f

    /**
     * 指示器当前位置
     */
    var currentPosition: Int = 0

    /**
     * 从一个点滑动到另一个点的进度
     */
    @Px
    var slideProgress: Float = 0f

    var showIndicatorOneItem: Boolean = false

    init {
        normalSliderWidth = 8F.px()
        checkedSliderWidth = normalSliderWidth
        sliderGap = normalSliderWidth
        normalSliderColor = Color.parseColor("#8C18171C")
        checkedSliderColor = Color.parseColor("#8C6C6D72")
        slideMode = IndicatorSlideMode.NORMAL
    }

    fun setCheckedColor(@ColorInt checkedColor: Int) {
        this.checkedSliderColor = checkedColor
    }

    fun setSliderWidth(
        @Px normalIndicatorWidth: Float,
        @Px checkedIndicatorWidth: Float
    ) {
        this.normalSliderWidth = normalIndicatorWidth
        this.checkedSliderWidth = checkedIndicatorWidth
    }

    fun setSliderWidth(@Px sliderWidth: Float) {
        setSliderWidth(sliderWidth, sliderWidth)
    }

    fun setSliderColor(
        @ColorInt normalColor: Int,
        @ColorInt checkedColor: Int
    ) {
        this.normalSliderColor = normalColor
        this.checkedSliderColor = checkedColor
    }
}
