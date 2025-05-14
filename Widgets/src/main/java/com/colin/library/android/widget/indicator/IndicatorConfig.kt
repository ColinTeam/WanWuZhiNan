package com.colin.library.android.widget.indicator

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.Px
import com.colin.library.android.widget.Constants
import com.colin.library.android.widget.def.Direction

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025/5/1 08:47
 *
 * Des   :IndicatorConfig
 */
class IndicatorConfig {
    private var indicatorCount = 0
    private var currentPosition = 0
    private var gravity = Direction.CENTER

    private var indicatorSpace = Constants.INDICATOR_SPACE
    private var normalWidth = Constants.INDICATOR_NORMAL_WIDTH
    private var selectedWidth = Constants.INDICATOR_SELECTED_WIDTH
    private var normalHeight = Constants.INDICATOR_SELECTED_WIDTH
    private var selectedHeight = Constants.INDICATOR_SELECTED_WIDTH

    @ColorInt
    private var normalColor = Color.GRAY

    @ColorInt
    private var selectedColor = Color.BLACK

    private var radius = Constants.INDICATOR_RADIUS
    private var height = Constants.INDICATOR_HEIGHT

    private var margins: Margins? = null

    //是将指示器添加到banner上
    private var attachToBanner = true

    fun getMargins(): Margins {
        if (margins == null) {
            setMargins(Margins())
        }
        return margins!!
    }

    fun setMargins(margins: Margins) = apply {
        this.margins = margins
    }

    fun getIndicatorCount(): Int {
        return indicatorCount
    }

    fun setIndicatorCount(count: Int) = apply {
        this.indicatorCount = count
    }

    @ColorInt
    fun getNormalColor(): Int {
        return normalColor
    }

    fun setNormalColor(@ColorInt color: Int) = apply {
        this.normalColor = color
    }

    @ColorInt
    fun getSelectedColor(): Int {
        return selectedColor
    }

    fun setSelectedColor(@ColorInt color: Int) = apply {
        this.selectedColor = color
    }

    fun getIndicatorSpace(): Float {
        return indicatorSpace
    }

    fun setIndicatorSpace(indicatorSpace: Float) = apply {
        this.indicatorSpace = indicatorSpace
    }

    fun getCurrentPosition(): Int {
        return currentPosition
    }

    fun setCurrentPosition(position: Int) = apply {
        this.currentPosition = position
    }

    fun setWidth(@Px normal: Float, selected: Float) = apply {
        this.normalWidth = normal
        this.selectedWidth = selected
    }

    fun setHeight(@Px normal: Float, selected: Float) = apply {
        this.normalHeight = normal
        this.selectedHeight = selected
    }

    fun setRadius(@Px radius: Float) = apply {
        this.radius = radius
    }

    fun setColor(@ColorInt normal: Int, @ColorInt selected: Int) = apply {
        this.normalColor = normal
        this.selectedColor = selected
    }

    fun getNormalWidth(): Float {
        return normalWidth
    }

    fun setNormalWidth(width: Float) = apply {
        this.normalWidth = width
    }

    fun getSelectedWidth(): Float {
        return selectedWidth
    }

    fun setSelectedWidth(width: Float) = apply {
        this.selectedWidth = width
    }

    @Direction
    fun getGravity(): Int {
        return gravity
    }

    fun setGravity(@Direction gravity: Int) = apply {
        this.gravity = gravity
    }

    fun isAttachToBanner(): Boolean {
        return attachToBanner
    }

    fun setAttachToBanner(attach: Boolean) = apply {
        this.attachToBanner = attach
    }

    fun getRadius(): Float {
        return radius
    }


    fun getHeight(): Float {
        return height
    }

    fun setHeight(height: Float) = apply {
        this.height = height
    }

    class Margins {
        var top = 0F
        var bottom = 0F
        var left = 0F
        var right = 0F

        constructor() : this(Constants.INDICATOR_MARGIN)

        constructor(margin: Float) : this(margin, margin, margin, margin)

        constructor(top: Float, bottom: Float, left: Float, right: Float) {
            this.top = top
            this.bottom = bottom
            this.left = left
            this.right = right
        }
    }

    companion object {
        @JvmStatic
        fun createMargins(space: Float): Margins {
            return Margins(space, space, space, space)
        }

        @JvmStatic
        fun createMargins(top: Float, bottom: Float, left: Float, right: Float): Margins {
            return Margins(top, bottom, left, right)
        }
    }
}