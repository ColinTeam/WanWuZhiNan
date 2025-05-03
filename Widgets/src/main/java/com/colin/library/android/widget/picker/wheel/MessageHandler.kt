package com.colin.library.android.widget.picker.wheel

import android.os.Handler
import android.os.Looper
import android.os.Message
import java.lang.ref.WeakReference

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-02 09:24
 *
 * Des   :MessageHandler
 */
class MessageHandler(wheelView: WheelView?) : Handler(Looper.getMainLooper()) {
    private val wheelView: WeakReference<WheelView?> = WeakReference<WheelView?>(wheelView)

    override fun handleMessage(msg: Message) {
        val view: WheelView? = wheelView.get()
        if (view == null) return
        when (msg.what) {
            WHAT_INVALIDATE_LOOP_VIEW -> view.invalidate()
            WHAT_SMOOTH_SCROLL -> view.smoothScroll(WheelView.ACTION.FLING)
            WHAT_ITEM_SELECTED -> view.onItemSelected()
        }
    }

    companion object {
        const val WHAT_INVALIDATE_LOOP_VIEW: Int = 1000
        const val WHAT_SMOOTH_SCROLL: Int = 2000
        const val WHAT_ITEM_SELECTED: Int = 3000
    }
}
