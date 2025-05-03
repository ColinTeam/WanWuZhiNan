package com.colin.library.android.widget.indicator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025/5/1 09:31
 *
 * Des   :RoundLinesIndicator
 */
class RoundLinesIndicator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseIndicator(context, attrs, defStyleAttr) {
    private var rectF: RectF = RectF(0F, 0F, 0F, 0F)

    init {
        paint.style = Paint.Style.FILL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val count = config.getIndicatorCount()
        if (count <= 1) return
        setMeasuredDimension(
            (config.getSelectedWidth() * count).toInt(), config.getHeight().toInt()
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val count = config.getIndicatorCount()
        if (count <= 1) return
        paint.color = config.getNormalColor()
        rectF.set(0F, 0F, width.toFloat(), config.getHeight())
        canvas.drawRoundRect(rectF, config.getRadius(), config.getRadius(), paint)
        paint.color = config.getSelectedColor()
        val left = config.getCurrentPosition() * config.getSelectedWidth()
        rectF.set(left, 0f, (left + config.getSelectedWidth()), config.getHeight())
        canvas.drawRoundRect(rectF, config.getRadius(), config.getRadius(), paint)
    }
}