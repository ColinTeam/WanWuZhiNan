package com.colin.library.android.widget.edit

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.text.TextPaint
import android.util.AttributeSet
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.colin.library.android.utils.ext.dp
import com.colin.library.android.widget.R
import java.nio.CharBuffer


class CodeEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {
    private var mTextColor = Color.BLACK

    // 输入的最大长度
    private var mMaxLength = 6

    // 边框宽度
    private var mStrokeWidth = 1.0f

    // 边框高度
    private var mStrokeHeight = mStrokeWidth

    // 边框之间的距离
    private var mStrokePadding = 10.0f

    // 光标宽度
    private var mCursorWidth = mStrokeWidth

    // 光标高度
    private var mCursorHeight = mStrokeHeight - 2.0f

    // 方框的背景
    private var mStrokeDrawable: Drawable? = null

    // 光标的背景
    private var mCursorDrawable: Drawable? = null

    // 输入结束监听
    private var mOnInputFinishListener: OnTextFinishListener? = null

    // 是否光标获取焦点
    private var mCursorStateFocused = true

    // 记录上次光标获取焦点时间
    private var mLastCursorFocusedTimeMillis = System.currentTimeMillis()

    private val rect = Rect()

    init {
        this.mStrokeWidth = 40F.dp()
        this.mStrokeHeight = 40F.dp()
        this.mStrokePadding = 12F.dp()
        this.mCursorWidth = 1F.dp()
        this.mCursorHeight = 25F.dp()
        this.mStrokeDrawable =
            ResourcesCompat.getDrawable(resources, R.drawable.edit_code_bg, context.theme)
        this.mCursorDrawable =
            ResourcesCompat.getDrawable(resources, R.drawable.edit_cursor_style, context.theme)
        this.mMaxLength = 6
        this.setMaxLength(mMaxLength)
        this.setBackgroundDrawable(null)
        this.isLongClickable = false
        this.setTextIsSelectable(false)
        this.isCursorVisible = false
        this.isFocusable = true
        this.isFocusableInTouchMode = true
    }

    override fun onTextContextMenuItem(id: Int): Boolean {
        return false
    }

    override fun onTextChanged(
        text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        // 当前文本长度
        val textLength = editableText.length
        if (textLength == mMaxLength) {
            this.clearFocus()
            hideSoftInput()
            mOnInputFinishListener?.onTextFinish(editableText.toString(), mMaxLength)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var width = measuredWidth
        var height = measuredHeight
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        // 判断高度是否小于推荐高度
        if (height < mStrokeHeight) height = mStrokeHeight.toInt()
        // 判断高度是否小于推荐宽度
        val recommendWidth = mStrokeWidth * mMaxLength + mStrokePadding * (mMaxLength - 1)
        if (width < recommendWidth) width = recommendWidth.toInt()

        val mWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, widthMode)
        val mHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, heightMode)

        setMeasuredDimension(mWidthMeasureSpec, mHeightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        this.mTextColor = currentTextColor
        this.setTextColor(Color.TRANSPARENT)
        super.onDraw(canvas)
        this.setTextColor(mTextColor)
        // 重绘背景颜色
        drawStrokeBackground(canvas)
        drawCursorBackground(canvas)
        drawText(canvas)
    }

    /**
     * 重绘背景
     */
    private fun drawStrokeBackground(canvas: Canvas) {
        if (mCursorDrawable == null || mMaxLength <= 0) return
        // 绘制方框背景
        rect.left = 0
        rect.top = 0
        rect.right = mStrokeWidth.toInt()
        rect.bottom = mStrokeHeight.toInt()
        val count = canvas.saveCount
        canvas.save()
        for (i in 0 until mMaxLength) {
            mStrokeDrawable!!.bounds = rect
            mStrokeDrawable!!.state = intArrayOf(android.R.attr.state_enabled)
            mStrokeDrawable!!.draw(canvas)
            val dx = rect.right + mStrokePadding
            // 移动画布
            canvas.save()
            canvas.translate(dx, 0.0f)
        }
        canvas.restoreToCount(count)
        canvas.translate(0.0f, 0.0f)
        // 绘制激活状态
        // 当前激活的索引
        val activatedIndex = 0.coerceAtLeast(editableText.length)
        if (activatedIndex < mMaxLength) {
            rect.left = (mStrokeWidth * activatedIndex + mStrokePadding * activatedIndex).toInt()
            rect.right = (rect.left + mStrokeWidth).toInt()
            mStrokeDrawable!!.state = intArrayOf(android.R.attr.state_focused)
            mStrokeDrawable!!.bounds = rect
            mStrokeDrawable!!.draw(canvas)
        }
    }


    /**
     *重绘光标
     */
    private fun drawCursorBackground(canvas: Canvas) {
        if (mCursorDrawable == null || mMaxLength <= 0) return
        // 绘制光标
        rect.left = ((mStrokeWidth - mCursorWidth) / 2).toInt()
        rect.top = ((mStrokeHeight - mCursorHeight) / 2).toInt()
        rect.right = (rect.left + mCursorWidth).toInt()
        rect.bottom = (rect.top + mCursorHeight).toInt()
        val count = canvas.saveCount
        canvas.save()
        for (i in 0 until mMaxLength) {
            mCursorDrawable!!.bounds = rect
            mCursorDrawable!!.state = intArrayOf(android.R.attr.state_enabled)
            mCursorDrawable!!.draw(canvas)
            val dx = rect.right + mStrokePadding
            // 移动画布
            canvas.save()
            canvas.translate(dx, 0.0f)
        }
        canvas.restoreToCount(count)
        canvas.translate(0.0f, 0.0f)
        // 绘制激活状态
        // 当前激活的索引
        val activatedIndex = 0.coerceAtLeast(editableText.length)
        if (activatedIndex < mMaxLength) {
            rect.left =
                (mStrokeWidth * activatedIndex + mStrokePadding * activatedIndex + (mStrokeWidth - mCursorWidth) / 2).toInt()
            rect.right = (rect.left + mCursorWidth).toInt()
            val s =
                if (isFocusable && isFocusableInTouchMode && mCursorStateFocused) android.R.attr.state_focused else android.R.attr.state_enabled
            mStrokeDrawable!!.state = intArrayOf(s)
            mStrokeDrawable!!.bounds = rect
            mStrokeDrawable!!.draw(canvas)
            if (System.currentTimeMillis() - mLastCursorFocusedTimeMillis >= 800) {
                mCursorStateFocused = !mCursorStateFocused
                mLastCursorFocusedTimeMillis = System.currentTimeMillis()
            }
        }

    }

    /**
     * 重绘文本
     */
    private fun drawText(canvas: Canvas) {
        canvas.translate(0.0f, 0.0f)
        val count = canvas.saveCount
        rect.setEmpty()
        val length = editableText.length
        for (i in 0 until length) {
            val buffer = CharBuffer.wrap(editableText)
            val text = buffer.charAt(i).toString()
            val textPaint: TextPaint = paint
            textPaint.color = mTextColor
            // 获取文本大小
            textPaint.getTextBounds(text, 0, 1, rect)
            // 计算(x,y) 坐标
            val x = mStrokeWidth / 2 + (mStrokeWidth + mStrokePadding) * i - rect.centerX()
            val y = (canvas.height + rect.height()) / 2
            canvas.drawText(text, x, y.toFloat(), textPaint)
        }
        canvas.restoreToCount(count)
    }


    private fun hideSoftInput() {
        val imm: InputMethodManager = ContextCompat.getSystemService(
            context, InputMethodManager::class.java
        ) ?: return
        imm.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    private fun setMaxLength(maxLength: Int) {
        this.mMaxLength = maxLength
        if (maxLength >= 0) {
            this.filters = arrayOf(InputFilter.LengthFilter(maxLength))
        } else {
            this.mMaxLength = 0
            this.filters = arrayOf(InputFilter.LengthFilter(mMaxLength))
        }
    }


    interface OnTextFinishListener {
        fun onTextFinish(content: String, length: Int)
    }

    fun addOnTextFinishListener(listener: OnTextFinishListener?) {
        this.mOnInputFinishListener = listener
    }
}

fun CharBuffer.charAt(index: Int) = this[position() + index]