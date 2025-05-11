package com.colin.library.android.widget.skeleton

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ComposeShader
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.Shader
import android.util.AttributeSet
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import com.colin.library.android.widget.R
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class ShimmerLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null as AttributeSet?, defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {
    private var maskOffsetX = 0
    private var maskRect: Rect? = null
    private var gradientTexturePaint: Paint? = null
    private var maskAnimator: ValueAnimator? = null
    private var localMaskBitmap: Bitmap? = null
    private var maskBitmap: Bitmap? = null
    private var canvasForShimmerMask: Canvas? = null
    private var isAnimationReversed = false
    private var isAnimationStarted = false
    private var autoStart = false
    private var shimmerAnimationDuration = 0
    private var shimmerColor = 0
    private var shimmerAngle = 0
    private var maskWidth = 0f
    private var gradientCenterColorWidth = 0f
    private var startAnimationPreDrawListener: ViewTreeObserver.OnPreDrawListener? = null

    init {
        this.setWillNotDraw(false)
        context.withStyledAttributes(attrs, R.styleable.ShimmerLayout, defStyle, 0) {
            shimmerAngle = getInteger(R.styleable.ShimmerLayout_shimmer_angle, 20)
            shimmerAnimationDuration =
                getInteger(R.styleable.ShimmerLayout_shimmer_animation_duration, 1500)
            shimmerColor = getColor(
                R.styleable.ShimmerLayout_shimmer_color, getColor(R.color.skeleton_shimmer_color)
            )
            autoStart = getBoolean(R.styleable.ShimmerLayout_shimmer_auto_start, false)
            maskWidth = getFloat(R.styleable.ShimmerLayout_shimmer_mask_width, 0.5f)
            gradientCenterColorWidth =
                getFloat(R.styleable.ShimmerLayout_shimmer_gradient_width, 0.1f)
            isAnimationReversed =
                getBoolean(R.styleable.ShimmerLayout_shimmer_reverse_animation, false)
        }



        this.setMaskWidth(this.maskWidth)
        this.setGradientCenterColorWidth(this.gradientCenterColorWidth)
        this.setShimmerAngle(this.shimmerAngle)
        if (this.autoStart && this.isVisible) {
            this.startShimmerAnimation()
        }
    }

    override fun onDetachedFromWindow() {
        this.resetShimmering()
        super.onDetachedFromWindow()
    }

    override fun dispatchDraw(canvas: Canvas) {
        if (this.isAnimationStarted && this.width > 0 && this.height > 0) {
            this.dispatchDrawShimmer(canvas)
        } else {
            super.dispatchDraw(canvas)
        }
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == 0) {
            if (this.autoStart) {
                this.startShimmerAnimation()
            }
        } else {
            this.stopShimmerAnimation()
        }
    }

    fun startShimmerAnimation() {
        if (!this.isAnimationStarted) {
            if (this.width == 0) {
                this.startAnimationPreDrawListener = object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        this@ShimmerLayout.getViewTreeObserver().removeOnPreDrawListener(this)
                        this@ShimmerLayout.startShimmerAnimation()
                        return true
                    }
                }
                this.getViewTreeObserver().addOnPreDrawListener(this.startAnimationPreDrawListener)
            } else {
                val animator: Animator = this.shimmerAnimation
                animator.start()
                this.isAnimationStarted = true
            }
        }
    }

    fun stopShimmerAnimation() {
        if (this.startAnimationPreDrawListener != null) {
            this.getViewTreeObserver().removeOnPreDrawListener(this.startAnimationPreDrawListener)
        }

        this.resetShimmering()
    }

    fun setShimmerColor(shimmerColor: Int) {
        this.shimmerColor = shimmerColor
        this.resetIfStarted()
    }

    fun setShimmerAnimationDuration(durationMillis: Int) {
        this.shimmerAnimationDuration = durationMillis
        this.resetIfStarted()
    }

    fun setAnimationReversed(animationReversed: Boolean) {
        this.isAnimationReversed = animationReversed
        this.resetIfStarted()
    }

    fun setShimmerAngle(angle: Int) {
        if (angle >= -45 && 45 >= angle) {
            this.shimmerAngle = angle
            this.resetIfStarted()
        } else {
            throw IllegalArgumentException(
                String.format(
                    "shimmerAngle value must be between %d and %d", -45, 45
                )
            )
        }
    }

    fun setMaskWidth(maskWidth: Float) {
        if (!(maskWidth <= 0.0f) && !(1.0f < maskWidth)) {
            this.maskWidth = maskWidth
            this.resetIfStarted()
        } else {
            throw IllegalArgumentException(
                String.format(
                    "maskWidth value must be higher than %d and less or equal to %d", 0, 1
                )
            )
        }
    }

    fun setGradientCenterColorWidth(gradientCenterColorWidth: Float) {
        if (!(gradientCenterColorWidth <= 0.0f) && !(1.0f <= gradientCenterColorWidth)) {
            this.gradientCenterColorWidth = gradientCenterColorWidth
            this.resetIfStarted()
        } else {
            throw IllegalArgumentException(
                String.format(
                    "gradientCenterColorWidth value must be higher than %d and less than %d", 0, 1
                )
            )
        }
    }

    private fun resetIfStarted() {
        if (this.isAnimationStarted) {
            this.resetShimmering()
            this.startShimmerAnimation()
        }
    }

    private fun dispatchDrawShimmer(canvas: Canvas) {
        super.dispatchDraw(canvas)
        this.localMaskBitmap = this.getMaskBitmap()
        if (this.localMaskBitmap != null) {
            if (this.canvasForShimmerMask == null) {
                this.canvasForShimmerMask = Canvas(this.localMaskBitmap!!)
            }

            this.canvasForShimmerMask!!.drawColor(0, PorterDuff.Mode.CLEAR)
            this.canvasForShimmerMask!!.save()
            this.canvasForShimmerMask!!.translate((-this.maskOffsetX).toFloat(), 0.0f)
            super.dispatchDraw(this.canvasForShimmerMask!!)
            this.canvasForShimmerMask!!.restore()
            this.drawShimmer(canvas)
            this.localMaskBitmap = null
        }
    }

    private fun drawShimmer(destinationCanvas: Canvas) {
        this.createShimmerPaint()
        destinationCanvas.save()
        destinationCanvas.translate(this.maskOffsetX.toFloat(), 0.0f)
        destinationCanvas.drawRect(
            this.maskRect!!.left.toFloat(),
            0.0f,
            this.maskRect!!.width().toFloat(),
            this.maskRect!!.height().toFloat(),
            this.gradientTexturePaint!!
        )
        destinationCanvas.restore()
    }

    private fun resetShimmering() {
        if (this.maskAnimator != null) {
            this.maskAnimator!!.end()
            this.maskAnimator!!.removeAllUpdateListeners()
        }

        this.maskAnimator = null
        this.gradientTexturePaint = null
        this.isAnimationStarted = false
        this.releaseBitMaps()
    }

    private fun releaseBitMaps() {
        this.canvasForShimmerMask = null
        if (this.maskBitmap != null) {
            this.maskBitmap!!.recycle()
            this.maskBitmap = null
        }
    }

    private fun getMaskBitmap(): Bitmap? {
        if (this.maskBitmap == null) {
            this.maskBitmap = this.createBitmap(this.maskRect!!.width(), this.height)
        }

        return this.maskBitmap
    }

    private fun createShimmerPaint() {
        if (this.gradientTexturePaint == null) {
            val edgeColor = this.reduceColorAlphaValueToZero(this.shimmerColor)
            val shimmerLineWidth = (this.getWidth() / 2).toFloat() * this.maskWidth
            val yPosition = if (0 <= this.shimmerAngle) this.getHeight().toFloat() else 0.0f
            val gradient = LinearGradient(
                0.0f,
                yPosition,
                cos(Math.toRadians(this.shimmerAngle.toDouble())).toFloat() * shimmerLineWidth,
                yPosition + sin(
                    Math.toRadians(this.shimmerAngle.toDouble())
                ).toFloat() * shimmerLineWidth,
                intArrayOf(edgeColor, this.shimmerColor, this.shimmerColor, edgeColor),
                this.gradientColorDistribution,
                Shader.TileMode.CLAMP
            )
            val maskBitmapShader =
                BitmapShader(this.localMaskBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            val composeShader = ComposeShader(gradient, maskBitmapShader, PorterDuff.Mode.DST_IN)
            this.gradientTexturePaint = Paint()
            this.gradientTexturePaint!!.isAntiAlias = true
            this.gradientTexturePaint!!.isDither = true
            this.gradientTexturePaint!!.isFilterBitmap = true
            this.gradientTexturePaint!!.setShader(composeShader)
        }
    }

    private val shimmerAnimation: Animator
        get() {
            if (this.maskAnimator != null) {
                return this.maskAnimator!!
            } else {
                if (this.maskRect == null) {
                    this.maskRect = this.calculateBitmapMaskRect()
                }

                val animationToX = this.width
                val animationFromX: Int
                if (this.width > this.maskRect!!.width()) {
                    animationFromX = -animationToX
                } else {
                    animationFromX = -this.maskRect!!.width()
                }

                val shimmerBitmapWidth = this.maskRect!!.width()
                val shimmerAnimationFullLength = animationToX - animationFromX
                this.maskAnimator = if (this.isAnimationReversed) ValueAnimator.ofInt(
                    *intArrayOf(
                        shimmerAnimationFullLength, 0
                    )
                ) else ValueAnimator.ofInt(*intArrayOf(0, shimmerAnimationFullLength))
                this.maskAnimator!!.setDuration(this.shimmerAnimationDuration.toLong())
                this.maskAnimator!!.repeatCount = -1
                this.maskAnimator!!.addUpdateListener(object :
                    ValueAnimator.AnimatorUpdateListener {
                    override fun onAnimationUpdate(animation: ValueAnimator) {
                        this@ShimmerLayout.maskOffsetX =
                            animationFromX + (animation.getAnimatedValue() as Int?)!!
                        if (this@ShimmerLayout.maskOffsetX + shimmerBitmapWidth >= 0) {
                            this@ShimmerLayout.invalidate()
                        }
                    }
                })
                return this.maskAnimator!!
            }
        }

    private fun createBitmap(width: Int, height: Int): Bitmap? {
        try {
            return Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8)
        } catch (var4: OutOfMemoryError) {
            System.gc()
            return null
        }
    }

    private fun getColor(id: Int): Int {
        return this.context.getColor(id)
    }

    private fun reduceColorAlphaValueToZero(actualColor: Int): Int {
        return Color.argb(
            0, Color.red(actualColor), Color.green(actualColor), Color.blue(actualColor)
        )
    }

    private fun calculateBitmapMaskRect(): Rect {
        return Rect(0, 0, this.calculateMaskWidth(), this.height)
    }

    private fun calculateMaskWidth(): Int {
        val shimmerLineBottomWidth = ((this.width / 2).toFloat() * this.maskWidth).toDouble() / cos(
            Math.toRadians(abs(this.shimmerAngle.toDouble()))
        )
        val shimmerLineRemainingTopWidth =
            this.height.toDouble() * tan(Math.toRadians(abs(this.shimmerAngle.toDouble())))
        return (shimmerLineBottomWidth + shimmerLineRemainingTopWidth).toInt()
    }

    private val gradientColorDistribution: FloatArray
        get() {
            val colorDistribution = FloatArray(4)
            colorDistribution[0] = 0.0f
            colorDistribution[3] = 1.0f
            colorDistribution[1] = 0.5f - this.gradientCenterColorWidth / 2.0f
            colorDistribution[2] = 0.5f + this.gradientCenterColorWidth / 2.0f
            return colorDistribution
        }

    companion object {
        private const val DEFAULT_ANIMATION_DURATION = 1500
        private const val DEFAULT_ANGLE: Byte = 20
        private val MIN_ANGLE_VALUE: Byte = -45
        private const val MAX_ANGLE_VALUE: Byte = 45
        private const val MIN_MASK_WIDTH_VALUE: Byte = 0
        private const val MAX_MASK_WIDTH_VALUE: Byte = 1
        private const val MIN_GRADIENT_CENTER_COLOR_WIDTH_VALUE: Byte = 0
        private const val MAX_GRADIENT_CENTER_COLOR_WIDTH_VALUE: Byte = 1
    }
}