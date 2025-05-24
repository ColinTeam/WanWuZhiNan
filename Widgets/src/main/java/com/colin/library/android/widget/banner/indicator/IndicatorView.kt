package com.colin.library.android.widget.banner.indicator

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.colin.library.android.widget.banner.base.BaseIndicatorView
import com.colin.library.android.widget.banner.controller.AttrsController
import com.colin.library.android.widget.banner.def.IndicatorOrientation
import com.colin.library.android.widget.banner.drawer.DrawerProxy
import com.colin.library.android.widget.banner.options.IndicatorOptions

/**
 * The Indicator in BannerViewPager，this include three indicator styles,as below:
 * [com.colin.library.android.widget.banner.def.IndicatorStyle.CIRCLE]
 * [com.colin.library.android.widget.banner.def.IndicatorStyle.DASH]
 * [com.colin.library.android.widget.banner.def.IndicatorStyle.ROUND_RECT]
 */
class IndicatorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseIndicatorView(context, attrs, defStyleAttr) {

    private var mDrawerProxy: DrawerProxy

    init {
        val options = getIndicatorOptions()
        AttrsController.initAttrs(context, attrs, options)
        mDrawerProxy = DrawerProxy(options)
    }

    override fun onLayout(
        changed: Boolean, left: Int, top: Int, right: Int, bottom: Int
    ) {
        super.onLayout(changed, left, top, right, bottom)
        mDrawerProxy.onLayout(changed, left, top, right, bottom)
    }

    override fun onMeasure(
        widthMeasureSpec: Int, heightMeasureSpec: Int
    ) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val measureResult = mDrawerProxy.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measureResult.measureWidth, measureResult.measureHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        rotateCanvas(canvas)
        mDrawerProxy.onDraw(canvas)
    }

    override fun setIndicatorOptions(options: IndicatorOptions) {
        super.setIndicatorOptions(options)
        mDrawerProxy.setIndicatorOptions(options)
    }


    override fun notifyDataChanged() {
        mDrawerProxy = DrawerProxy(getIndicatorOptions())
        super.notifyDataChanged()
    }

    private fun rotateCanvas(canvas: Canvas) {
        if (getIndicatorOptions().orientation == IndicatorOrientation.INDICATOR_VERTICAL) {
            canvas.rotate(90f, width / 2f, width / 2f)
        } else if (getIndicatorOptions().orientation == IndicatorOrientation.INDICATOR_RTL) {
            canvas.rotate(180f, width / 2f, height / 2f)
        }
    }

    fun setOrientation(@IndicatorOrientation orientation: Int) {
        getIndicatorOptions().orientation = orientation;
    }
}
