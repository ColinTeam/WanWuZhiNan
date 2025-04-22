package com.ssm.comm.ui.widget.tv

import android.text.Selection
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.widget.TextView

class CustomLinkMovementMethod private constructor() : LinkMovementMethod() {

    companion object {
        private var instance: CustomLinkMovementMethod? = null
            //自定义属性访问器
            get() {
                if (field == null) {
                    field = CustomLinkMovementMethod()
                }
                return field
            }

        //这里不能使用getInstance作为方法名，因为companion object内部已经有了getInstance这个方法了
        fun get(): CustomLinkMovementMethod {
            return instance!!
        }
    }

    private var mPressedSpan: ClickableSpan? = null

    override fun onTouchEvent(widget: TextView?, buffer: Spannable?, event: MotionEvent?): Boolean {
        if (event != null) {
            if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_DOWN) {
                mPressedSpan = getPressedSpan(widget!!, buffer!!, event)
                if (mPressedSpan != null) {
                    if (event.action == MotionEvent.ACTION_UP) {
                        mPressedSpan!!.onClick(widget)
                    } else if (event.action == MotionEvent.ACTION_DOWN) {
                        Selection.setSelection(
                            buffer,
                            buffer.getSpanStart(mPressedSpan),
                            buffer.getSpanEnd(mPressedSpan)
                        )
                    }
                }else{
                    Selection.removeSelection(buffer)
                }
            }else{
                mPressedSpan = null
                Selection.removeSelection(buffer)
            }
        }
        return mPressedSpan != null
    }

    /**
     * 获取点击区域的Clickspan对象
     *      —— 基本copy LinkMovementMethod
     * @param textView
     * @param spannable
     * @param event
     * @return
     */
    private fun getPressedSpan(
        textView: TextView,
        spannable: Spannable,
        event: MotionEvent
    ): ClickableSpan? {
        //触摸点相对于其所在组件原点的x坐标
        var x = event.x.toInt()
        var y = event.y.toInt()

        //获取触摸点距离可绘制区域(即减去padding后的区域)边界的距离
        x -= textView.totalPaddingLeft
        y -= textView.totalPaddingTop

        //获取触摸点相对于屏幕原点的偏移量，一般为0，除非可以滑动。其中view.getScrollX()获取的不是当前View相对于屏幕原点的偏移量，
        // 而是当前View可绘制区域（显示区域，-padding）相对于屏幕原点在Y轴上的偏移量
        x += textView.scrollX
        y += textView.scrollY

        //该点击位置的行号----触摸点在textView中垂直方向上的行数值。参数是触摸点在Y轴上的偏移量
        val line = textView.layout.getLineForVertical(y)
        //该点击位置的字符索引-----触摸点在某一行水平方向上的偏移量。参数分别是：该行行数值 和 触摸点在该行X轴上的偏移量。
        // 此方法得到的该值会根据该行上的文字的多少而变化，并不是横向上的像素大小
        val off = textView.layout.getOffsetForHorizontal(line, x.toFloat())

        //获取点击位置存在的ClickableSpan对象
        val link = spannable.getSpans(off, off, ClickableSpan::class.java)
        var touchedSpan: ClickableSpan? = null
        if (link.isNotEmpty()) {
            touchedSpan = link[0]
        }
        return touchedSpan
    }
}