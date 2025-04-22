package com.comm.banner.config

import androidx.annotation.ColorInt
import com.comm.banner.annotation.Direction

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.banner.config
 * ClassName: IndicatorConfig
 * Author:ShiMing Shi
 * CreateDate: 2022/10/8 15:37
 * Email:shiming024@163.com
 * Description:
 */
class IndicatorConfig {

    private var indicatorSize = 0
    private var currentPosition = 0
    private var gravity = Direction.CENTER

    private var indicatorSpace = BannerConfig.INDICATOR_SPACE
    private var normalWidth = BannerConfig.INDICATOR_NORMAL_WIDTH
    private var selectedWidth = BannerConfig.INDICATOR_SELECTED_WIDTH

    @ColorInt
    private var normalColor = BannerConfig.INDICATOR_NORMAL_COLOR

    @ColorInt
    private var selectedColor = BannerConfig.INDICATOR_SELECTED_COLOR

    private var radius = BannerConfig.INDICATOR_RADIUS
    private var height = BannerConfig.INDICATOR_HEIGHT

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

    fun getIndicatorSize(): Int{
        return indicatorSize
    }

    fun setIndicatorSize(indicatorSize: Int) = apply {
        this.indicatorSize = indicatorSize
    }

    @ColorInt
    fun getNormalColor(): Int{
        return normalColor
    }

    fun setNormalColor(@ColorInt normalColor: Int) = apply {
        this.normalColor = normalColor
    }

    @ColorInt
    fun getSelectedColor(): Int{
        return selectedColor
    }

    fun setSelectedColor(@ColorInt selectedColor: Int) = apply {
        this.selectedColor = selectedColor
    }

    fun getIndicatorSpace(): Int{
        return indicatorSpace
    }

    fun setIndicatorSpace(indicatorSpace: Int) = apply {
        this.indicatorSpace = indicatorSpace
    }

    fun getCurrentPosition(): Int{
        return currentPosition
    }

    fun setCurrentPosition(position: Int) = apply {
        this.currentPosition = position
    }

    fun getNormalWidth(): Int{
        return normalWidth
    }

    fun setNormalWidth(width: Int) = apply {
        this.normalWidth = width
    }

    fun getSelectedWidth(): Int{
        return selectedWidth
    }

    fun setSelectedWidth(width: Int) = apply {
        this.selectedWidth = width
    }

    @Direction
    fun getGravity(): Int{
        return gravity
    }

    fun setGravity(@Direction gravity: Int) = apply {
        this.gravity = gravity
    }

    fun isAttachToBanner(): Boolean{
        return attachToBanner
    }

    fun setAttachToBanner(attach: Boolean) = apply {
        this.attachToBanner = attach
    }

    fun getRadius(): Int{
        return radius
    }

    fun setRadius(radius: Int) = apply {
        this.radius = radius
    }

    fun getHeight(): Int{
        return height
    }

    fun setHeight(height: Int) = apply {
        this.height = height
    }

    class Margins {
        var leftMargin = 0
        var topMargin = 0
        var rightMargin = 0
        var bottomMargin = 0

        constructor() : this(BannerConfig.INDICATOR_MARGIN)

        constructor(marginSize: Int) : this(marginSize, marginSize, marginSize, marginSize)

        constructor(leftMargin: Int, topMargin: Int, rightMargin: Int, bottomMargin: Int) {
            this.leftMargin = leftMargin
            this.topMargin = topMargin
            this.rightMargin = rightMargin
            this.bottomMargin = bottomMargin
        }
    }
}