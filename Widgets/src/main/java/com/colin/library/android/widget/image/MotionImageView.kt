package com.colin.library.android.widget.image

import android.content.Context
import android.util.AttributeSet
import com.colin.library.android.widget.motion.MotionHelper
import com.google.android.material.imageview.ShapeableImageView

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-08 10:39
 *
 * Des   :MotionImageView
 */
class MotionImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ShapeableImageView(context, attrs, defStyleAttr) {
    init {
        MotionHelper(this)
    }
}