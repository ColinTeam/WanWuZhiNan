package com.colin.library.android.widget.picker.adapter

import com.colin.library.android.utils.Constants

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-03 00:09
 *
 * Des   :NumericWheelAdapter
 */
class NumericWheelAdapter(private val minValue: Int, private val maxValue: Int) : WheelAdapter {

    override fun getItem(index: Int): Any {
        if (index >= 0 && index < getItemsCount()) {
            val value = minValue + index
            return value
        }
        return Constants.INVALID
    }

    override fun indexOf(data: Any): Int {
        return try {
            (data as Int) - minValue
        } catch (e: Exception) {
            -1
        }
    }

    override fun getItemsCount(): Int {
        return maxValue - minValue + 1
    }

}
