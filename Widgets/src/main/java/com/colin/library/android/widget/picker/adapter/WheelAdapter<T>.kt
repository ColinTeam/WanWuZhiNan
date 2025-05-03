package com.colin.library.android.widget.picker.adapter

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-02 09:05
 *
 * Des   :WheelAdapter<T>
 */
interface WheelAdapter {
    /**
     * Gets items count
     * @return the count of wheel items
     */
    fun getItemsCount(): Int

    /**
     * Gets a wheel item by index.
     * @param index the item index
     * @return the wheel item text or null
     */
    fun getItem(index: Int): Any

    /**
     * Gets maximum item length. It is used to determine the wheel width.
     * If -1 is returned there will be used the default wheel width.
     * @param data  the item object
     * @return the maximum item length or -1
     */
    fun indexOf(data: Any): Int
}