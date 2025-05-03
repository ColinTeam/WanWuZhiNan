package com.colin.library.android.widget.skeleton

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ComposeShader
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.core.view.isVisible
import com.colin.library.android.utils.Constants
import com.colin.library.android.widget.R
import java.util.Locale
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-01 10:10
 *
 * Des   :ShimmerLayout
 */
@SuppressLint("UseKtx")
class ShimmerLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {
    private val maskRect: RectF = RectF()
    private var gradientPaint: Paint? = null
    private var maskOffsetX = 0
    private var maskAnimator: ValueAnimator? = null

    private var localMaskBitmap: Bitmap? = null
    private var maskBitmap: Bitmap? = null
    private var canvasForShimmerMask: Canvas? = null

    private var animationReversed = false
    private var animationStarted = false
    private var autoStart = false

    private var animationDuration = Constants.ONE_SECOND.toLong()

    @ColorInt
    private var shimmerColor = Color.TRANSPARENT
    private var shimmerAngle = Constants.ZERO
    private var maskWidth = com.colin.library.android.widget.Constants.SHIMMER_MASK_WIDTH

    private var gradientWidth = com.colin.library.android.widget.Constants.SHIMMER_GRADIENT_WIDTH

    private var startAnimationPreDrawListener: ViewTreeObserver.OnPreDrawListener? = null

    init {
        setWillNotDraw(false)

        val array = context.theme.obtainStyledAttributes(
            attrs, R.styleable.ShimmerLayout, 0, 0
        )

        try {
            shimmerColor = array.getColor(
                R.styleable.ShimmerLayout_shimmer_color, getColor(R.color.skeleton_shimmer_color)
            )
            shimmerAngle = array.getInteger(
                R.styleable.ShimmerLayout_shimmer_angle,
                com.colin.library.android.widget.Constants.SHIMMER_ANGLE
            )
            animationDuration = array.getInteger(
                R.styleable.ShimmerLayout_shimmer_animation_duration, animationDuration.toInt()
            ).toLong()
            maskWidth =
                array.getFraction(R.styleable.ShimmerLayout_shimmer_mask_width, 1, 1, maskWidth)
            gradientWidth = array.getFraction(
                R.styleable.ShimmerLayout_shimmer_gradient_width, 1, 1, gradientWidth
            )
            autoStart = array.getBoolean(R.styleable.ShimmerLayout_shimmer_auto_start, false)
            animationReversed =
                array.getBoolean(R.styleable.ShimmerLayout_shimmer_reverse_animation, false)
        } finally {
            array.recycle()
        }
        setShimmerAngle(shimmerAngle)
        setMaskWidth(maskWidth)
        setGradientWidth(gradientWidth)
        if (autoStart && isVisible) startShimmerAnimation()
    }

    override fun onDetachedFromWindow() {
        resetShimmering()
        super.onDetachedFromWindow()
    }

    override fun dispatchDraw(canvas: Canvas) {
        if (!animationStarted || width <= 0 || height <= 0) super.dispatchDraw(canvas)
        else dispatchDrawShimmer(canvas)
    }

    override fun setVisibility(visibility: Int) {
        if (this.visibility == visibility) return
        super.setVisibility(visibility)
        if (visibility != VISIBLE) stopShimmerAnimation()
        else if (autoStart) startShimmerAnimation()
    }

    fun startShimmerAnimation() {
        if (animationStarted || width < 0) return
        if (width == 0) {
            startAnimationPreDrawListener = object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    getViewTreeObserver().removeOnPreDrawListener(this)
                    startShimmerAnimation()
                    return true
                }
            }
            getViewTreeObserver().addOnPreDrawListener(startAnimationPreDrawListener)
            return
        }

        val animator = getShimmerAnimation()
        animator.start()
        animationStarted = true
    }

    fun stopShimmerAnimation() {
        if (startAnimationPreDrawListener != null) {
            getViewTreeObserver().removeOnPreDrawListener(startAnimationPreDrawListener)
        }
        resetShimmering()
    }

    fun setShimmerColor(@ColorInt color: Int) {
        if (shimmerColor == color) return
        shimmerColor = color
        resetIfStarted()
    }

    fun setAnimationDuration(millis: Long) {
        if (animationDuration == millis) return
        animationDuration = millis
        resetIfStarted()
    }

    fun setAnimationReversed(reversed: Boolean) {
        if (animationReversed == reversed) return
        animationReversed = reversed
        resetIfStarted()
    }

    /**
     * Set the angle of the shimmer effect in clockwise direction in degrees.
     * The angle must be between {@value #MIN_ANGLE_VALUE} and {@value #MAX_ANGLE_VALUE}.
     *
     * @param angle The angle to be set
     */
    fun setShimmerAngle(@IntRange(from = -45, to = 45) angle: Int) {
        if (shimmerAngle == angle) return
        require(!(angle < -45 || 45 < angle)) {
            String.format(
                Locale.US, "shimmerAngle value must be between %d and %d", -45, 45
            )
        }
        this.shimmerAngle = angle
        resetIfStarted()
    }

    /**
     * Sets the width of the shimmer line to a value higher than 0 to less or equal to 1.
     * 1 means the width of the shimmer line is equal to half of the width of the ShimmerLayout.
     * The default value is 0.5.
     *
     * @param maskWidth The width of the shimmer line.
     */
    fun setMaskWidth(@FloatRange(from = 0.0, to = 1.0) fraction: Float) {
        if (maskWidth == fraction) return
        require(!(fraction <= 0F || 1F < fraction)) {
            String.format(
                Locale.US, "maskWidth value must be higher than %d and less or equal to %d", 0, 1
            )
        }
        this.maskWidth = fraction
        resetIfStarted()
    }

    /**
     * Sets the width of the center gradient color to a value higher than 0 to less than 1.
     * 0.99 means that the whole shimmer line will have this color with a little transparent edges.
     * The default value is 0.1.
     *
     * @param gradientWidth The width of the center gradient color.
     */
    fun setGradientWidth(@FloatRange(from = 0.0, to = 1.0) fraction: Float) {
        if (gradientWidth == fraction) return
        require(!(fraction <= 0F || 1F <= fraction)) {
            String.format(
                Locale.US, "gradientWidth value must be higher than %d and less than %d", 0, 1
            )
        }
        this.gradientWidth = fraction
        resetIfStarted()
    }

    private fun resetIfStarted() {
        if (animationStarted) {
            resetShimmering()
            startShimmerAnimation()
        }
    }

    private fun dispatchDrawShimmer(canvas: Canvas) {
        super.dispatchDraw(canvas)
        localMaskBitmap = getMaskBitmap() ?: return
        if (canvasForShimmerMask == null) {
            canvasForShimmerMask = Canvas(localMaskBitmap!!)
        }

        canvasForShimmerMask!!.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

        canvasForShimmerMask!!.save()
        canvasForShimmerMask!!.translate(-maskOffsetX.toFloat(), 0f)

        super.dispatchDraw(canvasForShimmerMask!!)

        canvasForShimmerMask!!.restore()

        drawShimmer(canvas)

        localMaskBitmap = null
    }

    private fun drawShimmer(destinationCanvas: Canvas) {
        createShimmerPaint()

        destinationCanvas.save()

        destinationCanvas.translate(maskOffsetX.toFloat(), 0f)
        destinationCanvas.drawRect(
            maskRect.left, 0f, maskRect.width(), maskRect.height(), gradientPaint!!
        )

        destinationCanvas.restore()
    }

    private fun resetShimmering() {
        if (maskAnimator != null) {
            maskAnimator!!.end()
            maskAnimator!!.removeAllUpdateListeners()
        }
        gradientPaint = null
        maskAnimator = null
        animationStarted = false
        releaseBitMaps()
    }

    private fun releaseBitMaps() {
        canvasForShimmerMask = null
        if (maskBitmap != null) {
            maskBitmap!!.recycle()
            maskBitmap = null
        }
    }

    private fun getMaskBitmap(): Bitmap? {
        if (maskBitmap == null) maskBitmap = createBitmap(maskRect.width().toInt(), height)
        return maskBitmap
    }

    private fun createShimmerPaint() {
        if (gradientPaint != null) {
            return
        }

        val edgeColor = reduceColorAlphaValueToZero(shimmerColor)
        val shimmerLineWidth = width / 2 * maskWidth
        val yPosition = (if (0 <= shimmerAngle) height else 0).toFloat()

        val gradient = LinearGradient(
            0f,
            yPosition,
            cos(Math.toRadians(shimmerAngle.toDouble())).toFloat() * shimmerLineWidth,
            yPosition + sin(Math.toRadians(shimmerAngle.toDouble())).toFloat() * shimmerLineWidth,
            intArrayOf(edgeColor, shimmerColor, shimmerColor, edgeColor),
            getGradientColorDistribution(),
            Shader.TileMode.CLAMP
        )

        val maskBitmapShader =
            BitmapShader(localMaskBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        val composeShader = ComposeShader(gradient, maskBitmapShader, PorterDuff.Mode.DST_IN)

        gradientPaint = Paint()
        gradientPaint!!.isAntiAlias = true
        gradientPaint!!.isDither = true
        gradientPaint!!.isFilterBitmap = true
        gradientPaint!!.setShader(composeShader)
    }

    private fun getShimmerAnimation(): Animator {
        if (maskAnimator != null) {
            return maskAnimator!!
        }

        calculateBitmapMaskRect()

        val animationToX = width
        val animationFromX: Int

        if (width > maskRect.width()) {
            animationFromX = -animationToX
        } else {
            animationFromX = -maskRect.width().toInt()
        }

        val shimmerBitmapWidth = maskRect.width()
        val shimmerAnimationFullLength = animationToX - animationFromX

        maskAnimator = if (animationReversed) ValueAnimator.ofInt(shimmerAnimationFullLength, 0)
        else ValueAnimator.ofInt(0, shimmerAnimationFullLength)
        maskAnimator!!.setDuration(animationDuration.toLong())
        maskAnimator!!.repeatCount = ObjectAnimator.INFINITE

        maskAnimator!!.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator) {
                maskOffsetX = animationFromX + animation.getAnimatedValue() as Int
                if (maskOffsetX + shimmerBitmapWidth >= 0) {
                    invalidate()
                }
            }
        })

        return maskAnimator!!
    }

    private fun createBitmap(width: Int, height: Int): Bitmap? {
        try {
            return Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8)
        } catch (e: OutOfMemoryError) {
            System.gc()
            return null
        }
    }

    private fun getColor(id: Int): Int {
        return context.getColor(id)
    }

    private fun reduceColorAlphaValueToZero(actualColor: Int): Int {
        return Color.argb(
            0, Color.red(actualColor), Color.green(actualColor), Color.blue(actualColor)
        )
    }

    private fun calculateBitmapMaskRect(): RectF {
        return RectF(0F, 0F, calculateMaskWidth(), height.toFloat())
    }

    private fun calculateMaskWidth(): Float {
        val shimmerLineRemainingTopWidth =
            height * tan(Math.toRadians(abs(shimmerAngle.toDouble()))).toFloat()

        val shimmerLineBottomWidth =
            (width / 2 * maskWidth) / cos(Math.toRadians(abs(shimmerAngle.toDouble())))

        return (shimmerLineBottomWidth + shimmerLineRemainingTopWidth).toFloat()
    }

    private fun getGradientColorDistribution(): FloatArray {
        val colorDistribution = FloatArray(4)
        colorDistribution[0] = 0f
        colorDistribution[3] = 1f
        colorDistribution[1] = 0.5f - gradientWidth / 2f
        colorDistribution[2] = 0.5f + gradientWidth / 2f
        return colorDistribution
    }
}