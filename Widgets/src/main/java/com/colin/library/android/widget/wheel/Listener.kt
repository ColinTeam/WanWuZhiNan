package com.colin.library.android.widget.wheel


/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-05 22:55
 *
 * Des   :Listener
 */
/**
 * WheelView 滚动停止后的选中监听器
 *
 */
interface OnItemSelectedListener {
    fun onItemSelected(wheelView: WheelView, position: Int)
}


/**
 * WheelView 滚动时 position 变化监听器
 *
 */
interface OnItemPositionChangedListener {
    fun onItemChanged(wheelView: WheelView, oldPosition: Int, newPosition: Int)
}


/**
 * WheelView 滚动变化监听器
 */
interface OnScrollChangedListener {

    fun onScrollChanged(wheelView: WheelView, scrollOffsetY: Int)

    fun onScrollStateChanged(wheelView: WheelView, state: Int)
}