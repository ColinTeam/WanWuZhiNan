package com.ad.img_load.dialog

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import com.ad.img_load.charAt
import com.comm.img_load.R
import java.nio.CharBuffer


class CodeEditText : AppCompatEditText {

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


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        this.mStrokeWidth = applyDimension(40.0f).toFloat()
        this.mStrokeHeight = applyDimension(40.0f).toFloat()
        this.mStrokePadding = applyDimension(12.0f).toFloat()
        this.mCursorWidth = applyDimension(1.0f).toFloat()
        this.mCursorHeight = applyDimension(25.0f).toFloat()
        this.mStrokeDrawable = resources.getDrawable(R.drawable.edit_code_bg)
        this.mCursorDrawable = resources.getDrawable(R.drawable.edit_cursor_style)
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
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        // 当前文本长度
        val textLength = editableText.length
        if (textLength == mMaxLength){
            this.clearFocus()
            hideSoftInput()
            mOnInputFinishListener?.onTextFinish(editableText.toString(),mMaxLength)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var width = measuredWidth
        var height = measuredHeight
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        // 判断高度是否小于推荐高度
        if (height < mStrokeHeight) {
            height = mStrokeHeight.toInt()
        }
        // 判断高度是否小于推荐宽度
        val recommendWidth = mStrokeWidth * mMaxLength + mStrokePadding * (mMaxLength - 1)
        if (width < recommendWidth) {
            width = recommendWidth.toInt()
        }

        val mWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, widthMode)
        val mHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, heightMode)

        setMeasuredDimension(mWidthMeasureSpec, mHeightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        this.mTextColor = currentTextColor
        this.setTextColor(Color.TRANSPARENT)
        super.onDraw(canvas)
        this.setTextColor(mTextColor)
        // 重绘背景颜色
        drawStrokeBackground(canvas!!)
        drawCursorBackground(canvas)
        drawText(canvas)
    }

    /**
     * 重绘背景
     */
    private fun drawStrokeBackground(canvas: Canvas) {
        val mRect = Rect()
        if (mStrokeDrawable != null) {
            // 绘制方框背景
            mRect.left = 0
            mRect.top = 0
            mRect.right = mStrokeWidth.toInt()
            mRect.bottom = mStrokeHeight.toInt()
            val count = canvas.saveCount
            canvas.save()
            for (i in 0 until mMaxLength) {
                mStrokeDrawable!!.bounds = mRect
                mStrokeDrawable!!.state = intArrayOf(android.R.attr.state_enabled)
                mStrokeDrawable!!.draw(canvas)
                val dx = mRect.right + mStrokePadding
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
                mRect.left =
                    (mStrokeWidth * activatedIndex + mStrokePadding * activatedIndex).toInt()
                mRect.right = (mRect.left + mStrokeWidth).toInt()
                mStrokeDrawable!!.state = intArrayOf(android.R.attr.state_focused)
                mStrokeDrawable!!.bounds = mRect
                mStrokeDrawable!!.draw(canvas)
            }
        }
    }


    /**
     *重绘光标
     */
    private fun drawCursorBackground(canvas: Canvas) {
        val mRect = Rect()
        if (mCursorDrawable != null) {
            // 绘制光标
            mRect.left = ((mStrokeWidth - mCursorWidth) / 2).toInt()
            mRect.top = ((mStrokeHeight - mCursorHeight) / 2).toInt()
            mRect.right = (mRect.left + mCursorWidth).toInt()
            mRect.bottom = (mRect.top + mCursorHeight).toInt()
            val count = canvas.saveCount
            canvas.save()
            for (i in 0 until mMaxLength) {
                mCursorDrawable!!.bounds = mRect
                mCursorDrawable!!.state = intArrayOf(android.R.attr.state_enabled)
                mCursorDrawable!!.draw(canvas)
                val dx = mRect.right + mStrokePadding
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
                mRect.left =
                    (mStrokeWidth * activatedIndex + mStrokePadding * activatedIndex + (mStrokeWidth - mCursorWidth) / 2).toInt()
                mRect.right = (mRect.left + mCursorWidth).toInt()
                val s =
                    if (isFocusable && isFocusableInTouchMode && mCursorStateFocused) android.R.attr.state_focused else android.R.attr.state_enabled
                mStrokeDrawable!!.state = intArrayOf(s)
                mStrokeDrawable!!.bounds = mRect
                mStrokeDrawable!!.draw(canvas)
                if (System.currentTimeMillis() - mLastCursorFocusedTimeMillis >= 800){
                    mCursorStateFocused = !mCursorStateFocused
                    mLastCursorFocusedTimeMillis = System.currentTimeMillis()
                }
            }
        }
    }

    /**
     * 重绘文本
     */
    private fun drawText(canvas: Canvas){
        val mRect = Rect()
        val count = canvas.saveCount
        canvas.translate(0.0f, 0.0f)
        val length = editableText.length
        for(i in 0 until length){
            val buffer = CharBuffer.wrap(editableText)
            val text = buffer.charAt(i).toString()
            val textPaint: TextPaint = paint
            textPaint.color = mTextColor
            // 获取文本大小
            textPaint.getTextBounds(text, 0, 1, mRect)
            // 计算(x,y) 坐标
            val x = mStrokeWidth / 2 + (mStrokeWidth + mStrokePadding) * i - mRect.centerX()
            val y = (canvas.height + mRect.height()) / 2
            canvas.drawText(text,x,y.toFloat(),textPaint)
        }
        canvas.restoreToCount(count)
    }


    private fun hideSoftInput(){
        val imm: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
    }

    private fun setMaxLength(maxLength: Int){
        this.mMaxLength = maxLength
        if (maxLength >= 0){
            this.filters = arrayOf(InputFilter.LengthFilter(maxLength))
        }else{
            this.mMaxLength = 0
            this.filters = arrayOf(InputFilter.LengthFilter(mMaxLength))
        }
    }

    /**
     * 转 dp 值
     * @param value
     * @return
     */
    private fun applyDimension(value: Float = 3.0f): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value,
            resources.displayMetrics
        ).toInt()
    }


    interface OnTextFinishListener {
        fun onTextFinish(content: String, length: Int)
    }

    fun addOnTextFinishListener(listener: OnTextFinishListener){
        this.mOnInputFinishListener = listener
    }
}