package com.colin.library.android.widget.picker.wheel

import java.lang.ref.WeakReference
import java.util.TimerTask
import kotlin.math.abs

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-02 21:33
 *
 * Des   :InertiaTimerTask:滚动惯性的实现
 */
class InertiaTimerTask(
    wheelView: WheelView, velocityY: Int
) : TimerTask() {
    private var mCurrentVelocityY: Int //当前滑动速度
    private val mFirstVelocityY: Int = velocityY //手指离开屏幕时的初始速度
    private val mWheelView: WeakReference<WheelView?> = WeakReference<WheelView?>(wheelView)

    /**
     * @param wheelView 滚轮对象
     * @param velocityY Y轴滑行速度
     */
    init {
        mCurrentVelocityY = Int.Companion.MAX_VALUE
    }

    override fun run() {
        //防止闪动，对速度做一个限制。
        val wheelView = mWheelView.get() ?: return
        if (mCurrentVelocityY == Int.Companion.MAX_VALUE) {
            mCurrentVelocityY = if (abs(mFirstVelocityY.toDouble()) > 2000) {
                if (mFirstVelocityY > 0) 2000 else -2000
            } else mFirstVelocityY
        }

        //发送handler消息 处理平顺停止滚动逻辑
        if (abs(mCurrentVelocityY) >= 0 && abs(mCurrentVelocityY) <= 20) {
            wheelView.cancelFuture()
            wheelView.handler?.sendEmptyMessage(MessageHandler.WHAT_SMOOTH_SCROLL)
            return
        }

        val dy = (mCurrentVelocityY / 100f).toInt()
        wheelView.setTotalScrollY(wheelView.getTotalScrollY() - dy)
        if (!wheelView.isLoop()) {
            val itemHeight: Float = wheelView.getItemHeight()
            var top: Float = (-wheelView.getInitPosition()) * itemHeight
            var bottom: Float =
                (wheelView.getItemsCount() - 1 - wheelView.getInitPosition()) * itemHeight
            if (wheelView.getTotalScrollY() - itemHeight * 0.25 < top) {
                top = wheelView.getTotalScrollY() + dy
            } else if (wheelView.getTotalScrollY() + itemHeight * 0.25 > bottom) {
                bottom = wheelView.getTotalScrollY() + dy
            }

            if (wheelView.getTotalScrollY() <= top) {
                mCurrentVelocityY = 40
                wheelView.setTotalScrollY(top.toInt().toFloat())
            } else if (wheelView.getTotalScrollY() >= bottom) {
                wheelView.setTotalScrollY(bottom.toInt().toFloat())
                mCurrentVelocityY = -40
            }
        }

        mCurrentVelocityY = if (mCurrentVelocityY < 0) {
            mCurrentVelocityY + 20
        } else {
            mCurrentVelocityY - 20
        }

        //刷新UI
        wheelView.handler?.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW)
    }
}