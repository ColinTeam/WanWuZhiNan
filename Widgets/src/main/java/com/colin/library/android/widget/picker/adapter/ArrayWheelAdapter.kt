package com.colin.library.android.widget.picker.adapter

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-03 00:02
 *
 * Des   :ArrayWheelAdapter
 */
class ArrayWheelAdapter(private val items: List<Any>) : WheelAdapter {

    override fun getItem(index: Int): Any {
        if (index >= 0 && index < items.size) {
            return items[index]
        }
        throw ArrayIndexOutOfBoundsException("getItem index")
    }

    override fun indexOf(data: Any) = items.indexOf(data)

    override fun getItemsCount() = items.size

}
