package com.comm.banner.indicator

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.banner.indicator
 * ClassName: CircleIndicator
 * Author:ShiMing Shi
 * CreateDate: 2022/10/9 13:30
 * Email:shiming024@163.com
 * Description:圆形指示器
 */
class CircleIndicator : BaseIndicator {

    private var mNormalRadius = 0
    private var mSelectedRadius = 0
    private var maxRadius = 0

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mNormalRadius = config!!.getNormalWidth() / 2
        mSelectedRadius = config!!.getSelectedWidth() / 2
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val count = config!!.getIndicatorSize()
        if (count <= 1) {
            return
        }
        mNormalRadius = config!!.getNormalWidth() / 2
        mSelectedRadius = config!!.getSelectedWidth() / 2
        //考虑当 选中和默认 的大小不一样的情况
        maxRadius = Math.max(mSelectedRadius, mNormalRadius)
        //间距*（总数-1）+选中宽度+默认宽度*（总数-1）
        val width =
            (count - 1) * config!!.getIndicatorSpace() + config!!.getSelectedWidth() + config!!.getNormalWidth() * (count - 1)
        setMeasuredDimension(
            width,
            config!!.getNormalWidth().coerceAtLeast(config!!.getSelectedWidth())
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val count = config!!.getIndicatorSize()
        if (count <= 1) {
            return
        }
        var left = 0f
        for (i in 0 until count) {
            mPaint!!.color =
                if (config!!.getCurrentPosition() == i) config!!.getSelectedColor() else config!!.getNormalColor()
            val indicatorWidth =
                if (config!!.getCurrentPosition() == i) config!!.getSelectedWidth() else config!!.getNormalWidth()
            val radius = if (config!!.getCurrentPosition() == i) mSelectedRadius else mNormalRadius
            canvas.drawCircle(left + radius, maxRadius.toFloat(), radius.toFloat(), mPaint!!)
            left += (indicatorWidth + config!!.getIndicatorSpace()).toFloat()
        }
//        mPaint.setColor(config.getNormalColor());
//        for (int i = 0; i < count; i++) {
//            canvas.drawCircle(left + maxRadius, maxRadius, mNormalRadius, mPaint);
//            left += config.getNormalWidth() + config.getIndicatorSpace();
//        }
//        mPaint.setColor(config.getSelectedColor());
//        left = maxRadius + (config.getNormalWidth() + config.getIndicatorSpace()) * config.getCurrentPosition();
//        canvas.drawCircle(left, maxRadius, mSelectedRadius, mPaint);
    }
}