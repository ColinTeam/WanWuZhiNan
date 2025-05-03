package com.colin.library.android.widget.indicator

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025/5/1 09:25
 *
 * Des   :RectangleIndicator
 *   1、可以设置选中和默认的宽度、指示器的圆角
 *   2、如果需要正方形将圆角设置为0，可将宽度和高度设置为一样
 *   3、如果不想选中时变长，可将选中的宽度和默认宽度设置为一样
 */
class RectangleIndicator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseIndicator(context, attrs, defStyleAttr) {

    private var rectF: RectF = RectF()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val count = config.getIndicatorCount()
        if (count <= 1) return
        //间距*（总数-1）+默认宽度*（总数-1）+选中宽度
        val space = config.getIndicatorSpace() * (count - 1)
        val normal = config.getNormalWidth() * (count - 1)
        val width = space + normal + config.getSelectedWidth()
        setMeasuredDimension(width.toInt(), config.getHeight().toInt())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val count = config.getIndicatorCount()
        if (count <= 1) return
        var left = 0f
        for (i in 0 until count) {
            paint.color =
                if (config.getCurrentPosition() == i) config.getSelectedColor() else config.getNormalColor()
            val width =
                if (config.getCurrentPosition() == i) config.getSelectedWidth() else config.getNormalWidth()
            rectF[left, 0f, left + width] = config.getHeight().toFloat()
            left += (width + config.getIndicatorSpace())
            canvas.drawRoundRect(
                rectF,
                config.getRadius().toFloat(),
                config.getRadius().toFloat(),
                paint
            )
        }
    }
}