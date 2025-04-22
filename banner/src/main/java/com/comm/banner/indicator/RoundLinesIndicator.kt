package com.comm.banner.indicator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.banner.indicator
 * ClassName: RoundLinesIndicator
 * Author:ShiMing Shi
 * CreateDate: 2022/10/9 13:34
 * Email:shiming024@163.com
 * Description:
 */
class RoundLinesIndicator: BaseIndicator {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mPaint!!.style = Paint.Style.FILL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val count = config!!.getIndicatorSize()
        if (count <= 1) return
        setMeasuredDimension((config!!.getSelectedWidth() * count), config!!.getHeight())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val count = config!!.getIndicatorSize()
        if (count <= 1) return
        mPaint!!.color = config!!.getNormalColor()
        val oval = RectF(0f, 0f, canvas.width.toFloat(), config!!.getHeight().toFloat())
        canvas.drawRoundRect(
            oval, config!!.getRadius().toFloat(), config!!.getRadius().toFloat(),
            mPaint!!
        )
        mPaint!!.color = config!!.getSelectedColor()
        val left = config!!.getCurrentPosition() * config!!.getSelectedWidth()
        val rectF = RectF(
            left.toFloat(),
            0f,
            (left + config!!.getSelectedWidth()).toFloat(),
            config!!.getHeight().toFloat()
        )
        canvas.drawRoundRect(
            rectF, config!!.getRadius().toFloat(), config!!.getRadius().toFloat(),
            mPaint!!
        )
    }
}