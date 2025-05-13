package com.colin.library.android.widget.image

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.withStyledAttributes
import com.colin.library.android.widget.R
import com.google.android.material.imageview.ShapeableImageView
import kotlin.math.abs

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-13 11:21
 *
 * Des   :RectangleImageView
 * 默认为正方形，按照宽设置高度
 */
class RectangleImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ShapeableImageView(context, attrs, defStyleAttr) {
    init {
        context.withStyledAttributes(attrs, R.styleable.SquareImageView, defStyleAttr, 0) {
            byWidth = getBoolean(R.styleable.SquareImageView_byWidth, true)
            ratio = getFloat(R.styleable.SquareImageView_ratio, 1.0f)
        }
    }

    var ratio = 1.0F
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }
    var byWidth = true
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        var desiredWidth = widthSize
        var desiredHeight = heightSize

        if (byWidth && widthMode != MeasureSpec.UNSPECIFIED) {
            // 以宽度为基准，根据比例计算高度
            desiredHeight = (widthSize / abs(ratio)).toInt()
        } else if (!byWidth && heightMode != MeasureSpec.UNSPECIFIED) {
            // 以高度为基准，根据比例计算宽度
            desiredWidth = (heightSize * abs(ratio)).toInt()
        }
        // 确保尺寸合法
        desiredWidth = desiredWidth.coerceAtLeast(0)
        desiredHeight = desiredHeight.coerceAtLeast(0)

        setMeasuredDimension(desiredWidth, desiredHeight)
    }
}