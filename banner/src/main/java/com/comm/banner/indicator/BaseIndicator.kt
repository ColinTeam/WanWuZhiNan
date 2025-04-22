package com.comm.banner.indicator

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.comm.banner.annotation.Direction
import com.comm.banner.config.IndicatorConfig

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.banner.indicator
 * ClassName: BaseIndicator
 * Author:ShiMing Shi
 * CreateDate: 2022/10/9 13:24
 * Email:shiming024@163.com
 * Description:
 */
open class BaseIndicator: View, Indicator {

    protected var config: IndicatorConfig? = null
    protected var mPaint: Paint? = null
    protected var offset: Float? = null

    constructor(context: Context): this(context,null)

    constructor(context: Context,attrs: AttributeSet?): this(context,attrs,0)

    constructor(context: Context,attrs: AttributeSet?,defStyleAttr: Int): super(context, attrs, defStyleAttr){
        this.config = IndicatorConfig()
        this.mPaint = Paint()
        this.mPaint!!.isAntiAlias = true
        this.mPaint!!.color = config!!.getNormalColor()
    }

    override fun getIndicatorView(): View {
        if (config!!.isAttachToBanner()) {
            val layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            when (config!!.getGravity()) {
                Direction.LEFT -> layoutParams.gravity = Gravity.BOTTOM or Gravity.START
                Direction.CENTER -> layoutParams.gravity =
                    Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                Direction.RIGHT -> layoutParams.gravity = Gravity.BOTTOM or Gravity.END
            }
            layoutParams.leftMargin = config!!.getMargins().leftMargin
            layoutParams.rightMargin = config!!.getMargins().rightMargin
            layoutParams.topMargin = config!!.getMargins().topMargin
            layoutParams.bottomMargin = config!!.getMargins().bottomMargin
            setLayoutParams(layoutParams)
        }
        return this
    }

    override fun getIndicatorConfig(): IndicatorConfig {
        return config!!
    }

    override fun onPageChanged(count: Int, currentPosition: Int) {
        config!!.setIndicatorSize(count)
        config!!.setCurrentPosition(currentPosition)
        requestLayout()
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        offset = positionOffset
        invalidate()
    }

    override fun onPageSelected(position: Int) {
        config!!.setCurrentPosition(position)
        invalidate()
    }

    override fun onPageScrollStateChanged(state: Int) {

    }
}