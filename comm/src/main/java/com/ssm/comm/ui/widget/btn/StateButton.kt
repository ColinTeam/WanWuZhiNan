package com.ssm.comm.ui.widget.btn

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.setPadding
import com.ssm.comm.R

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.ui.widget.btn
 * ClassName: StateButton
 * Author:ShiMing Shi
 * CreateDate: 2022/9/6 12:59
 * Email:shiming024@163.com
 * Description:
 */
class StateButton : AppCompatButton {

    @ColorInt
    private var mNormalTextColor = 0

    @ColorInt
    private var mPressedTextColor = 0

    @ColorInt
    private var mUnableTextColor = 0

    private var mTextColorStateList: ColorStateList

    //animation duration
    private var mDuration = 0

    //radius
    private var mRadius = 0f
    private var mRound = false

    //stroke
    private var mStrokeDashWidth = 0f
    private var mStrokeDashGap = 0f
    private var mNormalStrokeWidth = 0
    private var mPressedStrokeWidth = 0
    private var mUnableStrokeWidth = 0

    @ColorInt
    private var mNormalStrokeColor = 0

    @ColorInt
    private var mPressedStrokeColor = 0

    @ColorInt
    private var mUnableStrokeColor = 0

    //background color
    @ColorInt
    private var mNormalBackgroundColor = 0

    @ColorInt
    private var mPressedBackgroundColor = 0

    @ColorInt
    private var mUnableBackgroundColor = 0

    //Orientation
    private var normalOrientation = 0
    private var pressedOrientation = 0
    private var unableOrientation = 0

    //shape type
    private var normalShapeType = 0
    private var pressedShapeType = 0
    private var unableShapeType = 0

    private var mNormalBackground: GradientDrawable? = null
    private var mPressedBackground: GradientDrawable? = null
    private var mUnableBackground: GradientDrawable? = null

    private val states = Array(4) { IntArray(0) }

    private var mStateBackground: StateListDrawable? = null

    constructor(mContext: Context) : this(mContext, null)

    constructor(mContext: Context, mAttributeSet: AttributeSet?) : this(mContext, mAttributeSet, 0)

    constructor(mContext: Context, mAttributeSet: AttributeSet?, mDefStyleAttr: Int) : super(
        mContext,
        mAttributeSet,
        mDefStyleAttr
    ) {
        //set text color
        this.mTextColorStateList = textColors
        this.gravity = Gravity.CENTER
        this.setPadding(0)
        initViews(mContext, mAttributeSet)
    }

    private fun initViews(context: Context, attr: AttributeSet?) {
        val drawable = background
        mStateBackground = if (drawable is StateListDrawable) {
            drawable
        } else {
            StateListDrawable()
        }
        mNormalBackground = GradientDrawable()
        mPressedBackground = GradientDrawable()
        mUnableBackground = GradientDrawable()

        //pressed, focused, normal, unable
        states[0] = intArrayOf(android.R.attr.state_enabled, android.R.attr.state_pressed)
        states[1] = intArrayOf(android.R.attr.state_enabled, android.R.attr.state_focused)
        states[3] = intArrayOf(-android.R.attr.state_enabled)
        states[2] = intArrayOf(android.R.attr.state_enabled)

        val a = context.obtainStyledAttributes(attr, R.styleable.StateButton)
        //get original text color as default
        val mDefaultNormalTextColor = mTextColorStateList.getColorForState(
            states[2],
            currentTextColor
        )
        val mDefaultPressedTextColor = mTextColorStateList.getColorForState(
            states[0],
            currentTextColor
        )
        val mDefaultUnableTextColor = mTextColorStateList.getColorForState(
            states[3],
            currentTextColor
        )
        mNormalTextColor =
            a.getColor(R.styleable.StateButton_normalTextColor, mDefaultNormalTextColor)
        mPressedTextColor =
            a.getColor(R.styleable.StateButton_pressedTextColor, mDefaultPressedTextColor)
        mUnableTextColor =
            a.getColor(R.styleable.StateButton_unableTextColor, mDefaultUnableTextColor)
        setTextColor()
        //set animation duration
        mDuration = a.getInteger(R.styleable.StateButton_animationDuration, mDuration)
        mStateBackground!!.setEnterFadeDuration(mDuration)
        mStateBackground!!.setExitFadeDuration(mDuration)

        normalOrientation = a.getInt(R.styleable.StateButton_normalOrientation, -1)
        pressedOrientation = a.getInt(R.styleable.StateButton_pressedOrientation, -1)
        unableOrientation = a.getInt(R.styleable.StateButton_unableOrientation, -1)

        normalShapeType = a.getInt(R.styleable.StateButton_normalShapeType, -1)
        pressedShapeType = a.getInt(R.styleable.StateButton_pressedShapeType, -1)
        unableShapeType = a.getInt(R.styleable.StateButton_unableShapeType, -1)

        if (normalOrientation != -1) {
            setGradient(a)
        } else {
            setBackGroundColor(a)
        }

        setShapeType()

        //set radius
        mRadius = a.getDimensionPixelSize(R.styleable.StateButton_radius, 0).toFloat()
        mRound = a.getBoolean(R.styleable.StateButton_is_round, false)
        mNormalBackground!!.cornerRadius = mRadius
        mPressedBackground!!.cornerRadius = mRadius
        mUnableBackground!!.cornerRadius = mRadius

        //set stroke
        mStrokeDashWidth =
            a.getDimensionPixelSize(R.styleable.StateButton_strokeDashWidth, 0).toFloat()
        mStrokeDashGap = a.getDimensionPixelSize(R.styleable.StateButton_strokeDashGap, 0).toFloat()
        mNormalStrokeWidth = a.getDimensionPixelSize(R.styleable.StateButton_normalStrokeWidth, 0)
        mPressedStrokeWidth = a.getDimensionPixelSize(R.styleable.StateButton_pressedStrokeWidth, 0)
        mUnableStrokeWidth = a.getDimensionPixelSize(R.styleable.StateButton_unableStrokeWidth, 0)
        mNormalStrokeColor = a.getColor(R.styleable.StateButton_normalStrokeColor, 0)
        mPressedStrokeColor = a.getColor(R.styleable.StateButton_pressedStrokeColor, 0)
        mUnableStrokeColor = a.getColor(R.styleable.StateButton_unableStrokeColor, 0)
        setStroke()

        //set background
        mStateBackground!!.addState(states[0], mPressedBackground)
        mStateBackground!!.addState(states[1], mPressedBackground)
        mStateBackground!!.addState(states[3], mUnableBackground)
        mStateBackground!!.addState(states[2], mNormalBackground)
        setBackgroundDrawable(mStateBackground)
        a.recycle()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.stateListAnimator = null
        }
    }

    //设置渐变参数
    private fun setGradient(a: TypedArray) {
        //设置渐变方向
        mNormalBackground!!.orientation = getOrientation(normalOrientation)
        mPressedBackground!!.orientation = getOrientation(pressedOrientation)
        mUnableBackground!!.orientation = getOrientation(unableOrientation)

        //设置渐变类型
        mNormalBackground!!.gradientType = GradientDrawable.LINEAR_GRADIENT
        mPressedBackground!!.gradientType = GradientDrawable.LINEAR_GRADIENT
        mUnableBackground!!.gradientType = GradientDrawable.LINEAR_GRADIENT

        //开始颜色
        //background start color
        val mNormalBackgroundStartColor: Int =
            a.getColor(R.styleable.StateButton_normalBackgroundStartColor, 0)
        val mPressedBackgroundStartColor: Int =
            a.getColor(R.styleable.StateButton_pressedBackgroundStartColor, 0)
        val mUnableBackgroundStartColor: Int =
            a.getColor(R.styleable.StateButton_unableBackgroundStartColor, 0)
        //中间颜色
        //background center color
        val mNormalBackgroundCenterColor: Int =
            a.getColor(R.styleable.StateButton_normalBackgroundCenterColor, 0)
        val mPressedBackgroundCenterColor: Int =
            a.getColor(R.styleable.StateButton_pressedBackgroundCenterColor, 0)
        val mUnableBackgroundCenterColor: Int =
            a.getColor(R.styleable.StateButton_unableBackgroundCenterColor, 0)
        //结束颜色
        //background end color
        val mNormalBackgroundEndColor: Int =
            a.getColor(R.styleable.StateButton_normalBackgroundEndColor, 0)
        val mPressedBackgroundEndColor: Int =
            a.getColor(R.styleable.StateButton_pressedBackgroundEndColor, 0)
        val mUnableBackgroundEndColor: Int =
            a.getColor(R.styleable.StateButton_unableBackgroundEndColor, 0)
        //渐变颜色数组
        //渐变颜色数组
        @ColorInt val normalColors = intArrayOf(
            mNormalBackgroundStartColor,
            mNormalBackgroundCenterColor,
            mNormalBackgroundEndColor
        )
        @ColorInt val pressedColors = intArrayOf(
            mPressedBackgroundStartColor,
            mPressedBackgroundCenterColor,
            mPressedBackgroundEndColor
        )
        @ColorInt val unableColors = intArrayOf(
            mUnableBackgroundStartColor,
            mUnableBackgroundCenterColor,
            mUnableBackgroundEndColor
        )
        mNormalBackground!!.colors = normalColors
        mPressedBackground!!.colors = pressedColors
        mUnableBackground!!.colors = unableColors
    }

    //setBackGroundColor
    private fun setBackGroundColor(a: TypedArray) {
        mNormalBackgroundColor = a.getColor(R.styleable.StateButton_normalBackgroundColor, 0)
        mPressedBackgroundColor = a.getColor(R.styleable.StateButton_pressedBackgroundColor, 0)
        mUnableBackgroundColor = a.getColor(R.styleable.StateButton_unableBackgroundColor, 0)
        mNormalBackground!!.setColor(mNormalBackgroundColor)
        mPressedBackground!!.setColor(mPressedBackgroundColor)
        mUnableBackground!!.setColor(mUnableBackgroundColor)
    }

    //set shapeType
    private fun setShapeType() {
        if (normalShapeType != -1) {
            mNormalBackground!!.shape = normalShapeType
        }
        if (pressedShapeType != -1) {
            mPressedBackground!!.shape = pressedShapeType
        }
        if (unableShapeType != -1) {
            mUnableBackground!!.shape = unableShapeType
        }
    }

    private fun getOrientation(orientation: Int): GradientDrawable.Orientation {
        return when (orientation) {
            0 -> GradientDrawable.Orientation.BL_TR
            1 -> GradientDrawable.Orientation.BOTTOM_TOP
            2 -> GradientDrawable.Orientation.BR_TL
            3 -> GradientDrawable.Orientation.LEFT_RIGHT
            4 -> GradientDrawable.Orientation.RIGHT_LEFT
            5 -> GradientDrawable.Orientation.TL_BR
            6 -> GradientDrawable.Orientation.TOP_BOTTOM
            7 -> GradientDrawable.Orientation.TR_BL
            else -> GradientDrawable.Orientation.BL_TR
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setRound(mRound)
    }

    /****************** stroke color *********************/
    fun setNormalStrokeColor(@ColorInt normalStrokeColor: Int) {
        this.mNormalStrokeColor = normalStrokeColor
        mNormalBackground?.let { setStroke(it, mNormalStrokeColor, mNormalStrokeWidth) }
    }

    fun setPressedStrokeWidth(pressedStrokeWidth: Int) {
        this.mPressedStrokeWidth = pressedStrokeWidth
        mPressedBackground?.let { setStroke(it, mPressedStrokeColor, mPressedStrokeWidth) }
    }

    fun setUnableStrokeWidth(unableStrokeWidth: Int) {
        this.mUnableStrokeWidth = unableStrokeWidth
        mUnableBackground?.let { setStroke(it, mUnableStrokeColor, mUnableStrokeWidth) }
    }

    fun setStateStrokeWidth(normal: Int, pressed: Int, unable: Int) {
        this.mNormalStrokeWidth = normal
        this.mPressedStrokeWidth = pressed
        this.mUnableStrokeWidth = unable
        setStroke()
    }

    fun setStrokeDash(strokeDashWidth: Float, strokeDashGap: Float) {
        this.mStrokeDashWidth = strokeDashWidth
        this.mStrokeDashGap = strokeDashGap
        setStroke()
    }

    private fun setStroke() {
        setStroke(mNormalBackground!!, mNormalStrokeColor, mNormalStrokeWidth)
        setStroke(mPressedBackground!!, mPressedStrokeColor, mPressedStrokeWidth)
        setStroke(mUnableBackground!!, mUnableStrokeColor, mUnableStrokeWidth)
    }

    private fun setStroke(mBackground: GradientDrawable, mStrokeColor: Int, mStrokeWidth: Int) {
        mBackground.setStroke(mStrokeWidth, mStrokeColor, mStrokeDashWidth, mStrokeDashGap)
    }

    /********************   radius  *******************************/
    private fun setRadius(@FloatRange(from = 0.0) radius: Float) {
        this.mRadius = radius
        mNormalBackground!!.cornerRadius = mRadius
        mPressedBackground!!.cornerRadius = mRadius
        mUnableBackground!!.cornerRadius = mRadius
    }

    private fun setRound(round: Boolean) {
        this.mRound = round
        val h = measuredHeight
        if (mRound) {
            setRadius(h / 2f)
        }
    }

    fun setRadius(radii: FloatArray) {
        this.mNormalBackground!!.cornerRadii = radii
        this.mPressedBackground!!.cornerRadii = radii
        this.mUnableBackground!!.cornerRadii = radii
    }

    /********************  background color  **********************/
    fun setStateBackgroundColor(
        @ColorInt normal: Int,
        @ColorInt pressed: Int,
        @ColorInt unable: Int
    ) {
        this.mNormalBackgroundColor = normal
        this.mPressedBackgroundColor = pressed
        this.mUnableBackgroundColor = unable
        this.mNormalBackground?.setColor(mNormalBackgroundColor)
        this.mPressedBackground?.setColor(mPressedBackgroundColor)
        this.mUnableBackground?.setColor(mUnableBackgroundColor)
    }

    fun setNormalBackgroundColor(@ColorInt normalBackgroundColor: Int) {
        this.mNormalBackgroundColor = normalBackgroundColor
        this.mNormalBackground?.setColor(mNormalBackgroundColor)
    }

    fun setPressedBackgroundColor(@ColorInt pressedBackgroundColor: Int) {
        this.mPressedBackgroundColor = pressedBackgroundColor
        this.mPressedBackground?.setColor(mPressedBackgroundColor)
    }

    fun setUnableBackgroundColor(@ColorInt unableBackgroundColor: Int) {
        this.mUnableBackgroundColor = unableBackgroundColor
        this.mUnableBackground?.setColor(mUnableBackgroundColor)
    }

    /*******************alpha animation duration********************/
    fun setAnimationDuration(@androidx.annotation.IntRange(from = 0) duration: Int) {
        this.mDuration = duration
        this.mStateBackground?.setEnterFadeDuration(mDuration)
    }

    /***************  text color   ***********************/
    private fun setTextColor() {
        val colors =
            intArrayOf(mPressedTextColor, mPressedTextColor, mNormalTextColor, mUnableTextColor)
        mTextColorStateList = ColorStateList(states, colors)
        setTextColor(mTextColorStateList)
    }

    fun setStateTextColor(@ColorInt normal: Int, @ColorInt pressed: Int, @ColorInt unable: Int) {
        this.mNormalTextColor = normal
        this.mPressedTextColor = pressed
        this.mUnableTextColor = unable
        setTextColor()
    }

    fun setNormalTextColor(@ColorInt normalTextColor: Int) {
        this.mNormalTextColor = normalTextColor
        setTextColor()
    }

    fun setPressedTextColor(@ColorInt pressedTextColor: Int) {
        this.mPressedTextColor = pressedTextColor
        setTextColor()
    }

    fun setUnableTextColor(@ColorInt unableTextColor: Int) {
        this.mUnableTextColor = unableTextColor
        setTextColor()
    }
}