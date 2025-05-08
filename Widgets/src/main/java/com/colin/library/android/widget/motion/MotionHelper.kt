package com.colin.library.android.widget.motion

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import com.colin.library.android.utils.ResourcesUtil
import com.colin.library.android.widget.R
import java.lang.ref.WeakReference

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-08 10:42
 *
 * Des   :MotionHelper
 */
class MotionHelper(var view: View) : IMotion {
    private val viewRef: WeakReference<View?> = WeakReference<View?>(view)

    companion object {
        const val MOTION_SCALE_MAX = 1.0F
        const val MOTION_SCALE_MIN = 0.95F
        const val MOTION_DURATION = 100L
    }

    init {

        view.setOnTouchListener(object : View.OnTouchListener {
            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(
                v: View?, event: MotionEvent?
            ): Boolean {
                v ?: return false
                event ?: return false
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> motionStart(v)
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> motionEnd(v)
                }
                return false
            }
        })
    }

    private fun motionStart(view: View) {
        val drawable = if (view is ImageView) {
            view.drawable ?: view.background
        } else view.background
        val color = ResourcesUtil.getColor(view.context, R.color.color_FFCCCCCC)
        drawable.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY)

        view.animate().scaleX(MOTION_SCALE_MIN).scaleY(MOTION_SCALE_MIN)
            .setDuration(MOTION_DURATION).start()
    }

    private fun motionEnd(view: View) {
        val drawable = if (view is ImageView) {
            view.drawable ?: view.background
        } else view.background
        drawable.clearColorFilter()
        view.animate().scaleX(MOTION_SCALE_MAX).scaleY(MOTION_SCALE_MAX)
            .setDuration(MOTION_DURATION).start()
    }
}