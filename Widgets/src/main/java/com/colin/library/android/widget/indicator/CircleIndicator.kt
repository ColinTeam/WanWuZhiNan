package com.colin.library.android.widget.indicator

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025/5/1 09:48
 *
 * Des   :圆形指示器
 */
class CircleIndicator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseIndicator(context, attrs, defStyleAttr) {

    private var mNormalRadius = config.getNormalWidth() / 2
    private var mSelectedRadius = config.getSelectedWidth() / 2
    private var maxRadius = 0F

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val count = config.getIndicatorCount()
        if (count <= 1) return
        mNormalRadius = config.getNormalWidth() / 2
        mSelectedRadius = config.getSelectedWidth() / 2
        maxRadius = mSelectedRadius.coerceAtLeast(mNormalRadius)
        val width =
            (count - 1) * config.getIndicatorSpace() + config.getSelectedWidth() + config.getNormalWidth() * (count - 1)
        val height = config.getNormalWidth().coerceAtLeast(config.getSelectedWidth())
        setMeasuredDimension(width.toInt(), height.toInt())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val count = config.getIndicatorCount()
        if (count <= 1) return
        var left = 0f
        for (i in 0 until count) {
            paint.color =
                if (config.getCurrentPosition() == i) config.getSelectedColor() else config.getNormalColor()
            val indicatorWidth =
                if (config.getCurrentPosition() == i) config.getSelectedWidth() else config.getNormalWidth()
            val radius = if (config.getCurrentPosition() == i) mSelectedRadius else mNormalRadius
            canvas.drawCircle(left + radius, maxRadius.toFloat(), radius.toFloat(), paint)
            left += (indicatorWidth + config.getIndicatorSpace()).toFloat()
        }
    }
}