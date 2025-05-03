package com.wanwuzhinan.mingchang.view

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import com.colin.library.android.utils.ResourcesUtil
import com.wanwuzhinan.mingchang.R


//图片按钮点击变灰或者变小
class ClickImageView : AppCompatImageView {

    var mType = 0//type0 变灰 type1 变小

    constructor(context: Context) : super(context) {
        initAttrs(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
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
                    animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).start()
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    removeFilter()
                    animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                }
            }
            this.onTouchEvent(event)
        }
    }

    /**
     * 设置滤镜
     */
    private fun setFilter() {
        //先获取设置的src图片
        var drawable = drawable
        //当src图片为Null，获取背景图片
        if (drawable == null) {
            drawable = background
        }
        drawable?.setColorFilter(
            ResourcesUtil.getColor(context, R.color.color_cccccc),
            PorterDuff.Mode.MULTIPLY
        )
    }

    /**
     * 清除滤镜
     */
    private fun removeFilter() {
        //先获取设置的src图片
        var drawable = drawable
        //当src图片为Null，获取背景图片
        if (drawable == null) {
            drawable = background
        }
        drawable?.clearColorFilter()
    }
}