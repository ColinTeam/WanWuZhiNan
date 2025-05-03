package com.colin.library.android.widget.indicator

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.colin.library.android.utils.Constants

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025/5/1 08:41
 *
 * Des   :BaseIndicator
 */
open class BaseIndicator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), IIndicator {

    protected var config: IndicatorConfig = IndicatorConfig()
    protected var paint: Paint = Paint().apply {
        isAntiAlias = true
        color = config.getNormalColor()
    }
    protected var offset: Float = Constants.INVALID.toFloat()


    override fun getIndicatorView(): View {
        if (config.isAttachToBanner()) {
            val layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = config.getGravity()
                topMargin = config.getMargins().top.toInt()
                bottomMargin = config.getMargins().bottom.toInt()
                leftMargin = config.getMargins().left.toInt()
                rightMargin = config.getMargins().right.toInt()
            }
            setLayoutParams(layoutParams)
        }
        return this
    }

    override fun getIndicatorConfig(): IndicatorConfig {
        return config
    }

    override fun onPageChanged(count: Int, current: Int) {
        config.setIndicatorCount(count).setCurrentPosition(current)
        requestLayout()
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        offset = positionOffset
        invalidate()
    }

    override fun onPageSelected(position: Int) {
        config.setCurrentPosition(position)
        invalidate()
    }

    override fun onPageScrollStateChanged(state: Int) {

    }
}