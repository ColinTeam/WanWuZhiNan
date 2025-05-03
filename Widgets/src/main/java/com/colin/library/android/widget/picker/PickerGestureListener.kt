package com.colin.library.android.widget.picker

import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import java.lang.ref.WeakReference

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-02 08:12
 *
 * Des   :PickerGestureListener
 */
class PickerGestureListener(view: View) : SimpleOnGestureListener() {
    private val viewRef: WeakReference<View?> = WeakReference<View?>(view)

    override fun onFling(
        e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float
    ): Boolean {
        viewRef.get()?.let {
            it.scrollBy(velocityX.toInt(), velocityY.toInt())
            return true
        }
        return super.onFling(e1, e2, velocityX, velocityY)
    }
}