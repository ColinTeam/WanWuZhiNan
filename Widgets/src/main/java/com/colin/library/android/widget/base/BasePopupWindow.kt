package com.colin.library.android.widget.base

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.PopupWindow
import androidx.core.graphics.drawable.toDrawable
import androidx.core.widget.PopupWindowCompat

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Date  :2024-12-12 11:25
 *
 * Des   :BasePopupWindow
 */
abstract class BasePopupWindow(
    private val builder: Builder<*, *>
) : PopupWindow(builder.getView(), builder.width, builder.height, builder.focusable) {

    open fun show(anchor: View, xOff: Int = 0, yOff: Int = 0, gravity: Int = Gravity.BOTTOM) {
        this.setBackgroundDrawable(builder.background)
        this.isOutsideTouchable = builder.outsideTouchable
        this.isClippingEnabled = builder.clippingEnable
        this.animationStyle = builder.animStyle
        this.setOnDismissListener(builder.dismissListener)
        PopupWindowCompat.showAsDropDown(this, anchor, xOff, yOff, gravity)
    }

    @Suppress("UNCHECKED_CAST")
    abstract class Builder<Returner, PopupWindow>(
        val context: Context,
        val width: Int = LayoutParams.WRAP_CONTENT,
        val height: Int = LayoutParams.WRAP_CONTENT
    ) {
        constructor(context: Context) : this(context, 0, 0)

        //设置背景为透明，以解决点击外部区域无法关闭的问题。
        @SuppressLint("UseKtx")
        var background: Drawable = Color.TRANSPARENT.toDrawable()

        //此方法必须搭配background背景效果使用，不然点击弹框布局外的地方不会隐藏
        var outsideTouchable: Boolean = true
        var clippingEnable: Boolean = true
        var focusable: Boolean = true
        var animStyle = R.style.Animation_Toast
        var dismissListener: OnDismissListener? = null

        /**
         *设置PopupWindow Window 背景颜色
         * @param drawable
         */
        fun setBackground(drawable: Drawable): Returner {
            this.background = drawable
            return this as Returner
        }

        /**
         * 可点击PopupWindow 内容View 之外的Outside是否可以消失，需要配合 #{setBackground()} 使用
         * @param touchable 是否可点击
         */
        fun setOutsideTouchable(touchable: Boolean): Returner {
            this.outsideTouchable = touchable
            return this as Returner
        }

        /**
         * 是否允许PopupWindow的范围超过屏幕范围
         * @param enable
         */
        fun setClippingEnable(enable: Boolean): Returner {
            this.clippingEnable = enable
            return this as Returner
        }

        /**
         * 是否获取焦点
         * @param focusable
         */
        fun setFocusable(focusable: Boolean): Returner {
            this.focusable = focusable
            return this as Returner
        }

        /**
         * 弹框消失回调
         * @param dismissListener
         */
        fun setDismissListener(dismissListener: OnDismissListener?): Returner {
            this.dismissListener = dismissListener
            return this as Returner
        }

        /**
         * 返回#PopupWindow 布局UI
         */
        abstract fun getView(): View
        abstract fun build(): PopupWindow
    }


}