package com.wanwuzhinan.mingchang.view

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout
import com.wanwuzhinan.mingchang.R


//图片按钮点击变灰或者变小
class ClickView : LinearLayout {

    var mType = 0//type0 变灰 type1 变小

    constructor(context: Context) : super(context) {
        initAttrs(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttrs(context, attrs)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClickImageView)
        mType = typedArray.getInt(R.styleable.ClickImageView_click_type, 0)
        typedArray.recycle()

        this.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    setFilter()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    removeFilter()
                }
            }
            this.onTouchEvent(event)
        }
    }

    /**
     * 设置滤镜
     */
    private fun setFilter() {
        background?.setColorFilter(context.resources.getColor(R.color.color_cccccc), PorterDuff.Mode.MULTIPLY)
    }

    /**
     * 清除滤镜
     */
    private fun removeFilter() {
        background?.clearColorFilter()
    }
}