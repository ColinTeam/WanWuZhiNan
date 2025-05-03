package com.colin.library.android.widget.picker.wheel

import java.lang.ref.WeakReference
import java.util.TimerTask
import kotlin.math.abs

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-02 21:55
 *
 * Des   :SmoothScrollTimerTask
 */
class SmoothScrollTimerTask(
    wheelView: WheelView, private val offset: Int
) : TimerTask() {
    private var realTotalOffset: Int
    private var realOffset: Int
    private val mWheelView: WeakReference<WheelView?> = WeakReference<WheelView?>(wheelView)

    init {
        realTotalOffset = Int.Companion.MAX_VALUE
        realOffset = 0
    }

    override fun run() {
        val wheelView = mWheelView.get() ?: return
        if (realTotalOffset == Int.Companion.MAX_VALUE) {
            realTotalOffset = offset
        }
        //把要滚动的范围细分成10小份，按10小份单位来重绘
        realOffset = (realTotalOffset.toFloat() * 0.1f).toInt()

        if (realOffset == 0) {
            realOffset = if (realTotalOffset < 0) -1 else 1
        }

        if (abs(realTotalOffset.toDouble()) <= 1) {
            wheelView.cancelFuture()
            wheelView.handler?.sendEmptyMessage(MessageHandler.WHAT_ITEM_SELECTED)
        } else {
            wheelView.setTotalScrollY(wheelView.getTotalScrollY() + realOffset)
            //这里如果不是循环模式，则点击空白位置需要回滚，不然就会出现选到－1 item的 情况
            if (!wheelView.isLoop()) {
                val itemHeight: Float = wheelView.getItemHeight()
                val top = (-wheelView.getInitPosition()).toFloat() * itemHeight
                val bottom =
                    (wheelView.getItemsCount() - 1 - wheelView.getInitPosition()).toFloat() * itemHeight
                if (wheelView.getTotalScrollY() <= top || wheelView.getTotalScrollY() >= bottom) {
                    wheelView.setTotalScrollY(wheelView.getTotalScrollY() - realOffset)
                    wheelView.cancelFuture()
                    wheelView.handler?.sendEmptyMessage(MessageHandler.WHAT_ITEM_SELECTED)
                    return
                }
            }
            wheelView.handler?.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW)
            realTotalOffset = realTotalOffset - realOffset
        }
    }
}
