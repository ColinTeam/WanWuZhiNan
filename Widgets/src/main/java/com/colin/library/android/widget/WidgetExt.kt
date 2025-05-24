package com.colin.library.android.widget

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-01 03:31
 *
 * Des   :WidgetExt
 */
/**
 * 设置View圆角
 */
fun View.setClipViewCornerRadius(radius: Int) {
    if (radius > 0) {
        this.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View?, outline: Outline?) {
                outline?.setRoundRect(0, 0, view?.width ?: 0, view?.height ?: 0, radius.toFloat())
            }
        }
        this.clipToOutline = true
    } else {
        this.clipToOutline = false
    }
}