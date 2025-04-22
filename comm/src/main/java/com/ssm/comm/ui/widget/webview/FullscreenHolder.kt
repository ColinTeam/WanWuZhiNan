package com.ssm.comm.ui.widget.webview

import android.content.Context
import android.graphics.Color
import android.view.MotionEvent
import android.widget.FrameLayout
import com.ssm.comm.R

class FullscreenHolder(context: Context) : FrameLayout(context) {

    init {
        this.setBackgroundColor(Color.BLACK)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return true
    }
}