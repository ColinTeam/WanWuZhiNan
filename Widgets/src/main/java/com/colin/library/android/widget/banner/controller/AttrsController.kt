package com.colin.library.android.widget.banner.controller

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.core.content.withStyledAttributes
import com.colin.library.android.widget.R
import com.colin.library.android.widget.banner.options.IndicatorOptions
import com.colin.library.android.widget.banner.utils.BannerUtils.dp2px

/**
 * Attrs控制器
 */
object AttrsController {
    fun initAttrs(
        context: Context, attrs: AttributeSet?, options: IndicatorOptions
    ) {
        if (attrs != null) {
            context.withStyledAttributes(attrs, R.styleable.IndicatorView) {
                val indicatorSlideMode = getInt(R.styleable.IndicatorView_banner_indicator_slide_mode, 0)
                val indicatorStyle = getInt(R.styleable.IndicatorView_banner_indicator_style, 0)
                val checkedColor = getColor(
                    R.styleable.IndicatorView_banner_indicator_slider_checked_color, Color.BLACK
                )
                val normalColor = getColor(
                    R.styleable.IndicatorView_banner_indicator_slider_normal_color, Color.GRAY
                )
                val orientation = getInt(R.styleable.IndicatorView_banner_indicator_orientation, 0)
                val radius = getDimension(
                    R.styleable.IndicatorView_banner_indicator_slider_radius, dp2px(8f).toFloat()
                )
                options.setCheckedColor(checkedColor)
                options.normalSliderColor = normalColor
                options.orientation = orientation
                options.indicatorStyle = indicatorStyle
                options.slideMode = indicatorSlideMode
                options.setSliderWidth(radius * 2, radius * 2)
            }
        }
    }
}