package com.colin.library.android.widget.image

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.core.graphics.createBitmap
import com.colin.library.android.widget.def.Corner
import java.lang.Float.isInfinite
import java.lang.Float.isNaN
import kotlin.math.max
import kotlin.math.min

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-01 22:12
 *
 * Des   :RoundedDrawable
 */
class RoundDrawable(private val bitmap: Bitmap) : Drawable() {
    private val mBounds = RectF()
    private val mDrawableRect = RectF()
    private val mBitmapRect = RectF()
    private val mBitmapPaint: Paint
    private val mBitmapWidth: Int = bitmap.width
    private val mBitmapHeight: Int = bitmap.height
    private val mBorderRect = RectF()
    private val mBorderPaint: Paint
    private val mShaderMatrix = Matrix()
    private val mSquareCornersRect = RectF()

    private var mTileModeX = Shader.TileMode.CLAMP
    private var mTileModeY = Shader.TileMode.CLAMP
    private var mRebuildShader = true

    private var mCornerRadius = 0f

    // [ topLeft, topRight, bottomLeft, bottomRight ]
    private val mCornersRounded = booleanArrayOf(true, true, true, true)

    private var mOval = false
    private var mBorderWidth = 0f
    private var mBorderColor = ColorStateList.valueOf(DEFAULT_BORDER_COLOR)
    private var mScaleType: ImageView.ScaleType? = ImageView.ScaleType.FIT_CENTER

    init {
        mBitmapRect.set(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat())

        mBitmapPaint = Paint().apply {
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        mBorderPaint = Paint().apply {
            style = Paint.Style.STROKE
            isAntiAlias = true
            strokeWidth = mBorderWidth
            color = mBorderColor.getColorForState(this@RoundDrawable.state, DEFAULT_BORDER_COLOR)
        }
    }


    override fun isStateful() = mBorderColor.isStateful

    override fun onStateChange(state: IntArray): Boolean {
        val newColor = mBorderColor.getColorForState(state, 0)
        if (mBorderPaint.color != newColor) {
            mBorderPaint.color = newColor
            return true
        } else {
            return super.onStateChange(state)
        }
    }

    private fun updateShaderMatrix() {
        val scale: Float
        var dx: Float
        var dy: Float
        when (mScaleType) {
            ImageView.ScaleType.CENTER -> {
                mBorderRect.set(mBounds)
                mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2)

                mShaderMatrix.reset()
                mShaderMatrix.setTranslate(
                    ((mBorderRect.width() - mBitmapWidth) * 0.5f + 0.5f).toInt().toFloat(),
                    ((mBorderRect.height() - mBitmapHeight) * 0.5f + 0.5f).toInt().toFloat()
                )
            }

            ImageView.ScaleType.CENTER_CROP -> {
                mBorderRect.set(mBounds)
                mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2)

                mShaderMatrix.reset()

                dx = .0f
                dy = .0f

                if (mBitmapWidth * mBorderRect.height() > mBorderRect.width() * mBitmapHeight) {
                    scale = mBorderRect.height() / mBitmapHeight.toFloat()
                    dx = (mBorderRect.width() - mBitmapWidth * scale) * 0.5f
                } else {
                    scale = mBorderRect.width() / mBitmapWidth.toFloat()
                    dy = (mBorderRect.height() - mBitmapHeight * scale) * 0.5f
                }

                mShaderMatrix.setScale(scale, scale)
                mShaderMatrix.postTranslate(
                    (dx + 0.5f).toInt() + mBorderWidth / 2, (dy + 0.5f).toInt() + mBorderWidth / 2
                )
            }

            ImageView.ScaleType.CENTER_INSIDE -> {
                mShaderMatrix.reset()

                if (mBitmapWidth <= mBounds.width() && mBitmapHeight <= mBounds.height()) {
                    scale = 1.0f
                } else {
                    scale = min(
                        (mBounds.width() / mBitmapWidth.toFloat()).toDouble(),
                        (mBounds.height() / mBitmapHeight.toFloat()).toDouble()
                    ).toFloat()
                }

                dx = ((mBounds.width() - mBitmapWidth * scale) * 0.5f + 0.5f).toInt().toFloat()
                dy = ((mBounds.height() - mBitmapHeight * scale) * 0.5f + 0.5f).toInt().toFloat()

                mShaderMatrix.setScale(scale, scale)
                mShaderMatrix.postTranslate(dx, dy)

                mBorderRect.set(mBitmapRect)
                mShaderMatrix.mapRect(mBorderRect)
                mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2)
                mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect, Matrix.ScaleToFit.FILL)
            }

            ImageView.ScaleType.FIT_CENTER -> {
                mBorderRect.set(mBitmapRect)
                mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.CENTER)
                mShaderMatrix.mapRect(mBorderRect)
                mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2)
                mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect, Matrix.ScaleToFit.FILL)
            }

            ImageView.ScaleType.FIT_END -> {
                mBorderRect.set(mBitmapRect)
                mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.END)
                mShaderMatrix.mapRect(mBorderRect)
                mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2)
                mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect, Matrix.ScaleToFit.FILL)
            }

            ImageView.ScaleType.FIT_START -> {
                mBorderRect.set(mBitmapRect)
                mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.START)
                mShaderMatrix.mapRect(mBorderRect)
                mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2)
                mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect, Matrix.ScaleToFit.FILL)
            }

            ImageView.ScaleType.FIT_XY -> {
                mBorderRect.set(mBounds)
                mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2)
                mShaderMatrix.reset()
                mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect, Matrix.ScaleToFit.FILL)
            }

            else -> {
                mBorderRect.set(mBitmapRect)
                mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.CENTER)
                mShaderMatrix.mapRect(mBorderRect)
                mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2)
                mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect, Matrix.ScaleToFit.FILL)
            }
        }

        mDrawableRect.set(mBorderRect)
        mRebuildShader = true
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)

        mBounds.set(bounds)

        updateShaderMatrix()
    }

    override fun draw(canvas: Canvas) {
        if (mRebuildShader) {
            val bitmapShader = BitmapShader(bitmap, mTileModeX, mTileModeY)
            if (mTileModeX == Shader.TileMode.CLAMP && mTileModeY == Shader.TileMode.CLAMP) {
                bitmapShader.setLocalMatrix(mShaderMatrix)
            }
            mBitmapPaint.shader = bitmapShader
            mRebuildShader = false
        }

        if (mOval) {
            if (mBorderWidth > 0) {
                canvas.drawOval(mDrawableRect, mBitmapPaint)
                canvas.drawOval(mBorderRect, mBorderPaint)
            } else {
                canvas.drawOval(mDrawableRect, mBitmapPaint)
            }
        } else {
            if (any(mCornersRounded)) {
                val radius = mCornerRadius
                if (mBorderWidth > 0) {
                    canvas.drawRoundRect(mDrawableRect, radius, radius, mBitmapPaint)
                    canvas.drawRoundRect(mBorderRect, radius, radius, mBorderPaint)
                    redrawBitmapForSquareCorners(canvas)
                    redrawBorderForSquareCorners(canvas)
                } else {
                    canvas.drawRoundRect(mDrawableRect, radius, radius, mBitmapPaint)
                    redrawBitmapForSquareCorners(canvas)
                }
            } else {
                canvas.drawRect(mDrawableRect, mBitmapPaint)
                if (mBorderWidth > 0) {
                    canvas.drawRect(mBorderRect, mBorderPaint)
                }
            }
        }
    }

    private fun redrawBitmapForSquareCorners(canvas: Canvas) {
        if (all(mCornersRounded)) {
            // no square corners
            return
        }

        if (mCornerRadius == 0f) {
            return  // no round corners
        }

        val left = mDrawableRect.left
        val top = mDrawableRect.top
        val right = left + mDrawableRect.width()
        val bottom = top + mDrawableRect.height()
        val radius = mCornerRadius

        if (!mCornersRounded[Corner.TOP_LEFT]) {
            mSquareCornersRect.set(left, top, left + radius, top + radius)
            canvas.drawRect(mSquareCornersRect, mBitmapPaint)
        }

        if (!mCornersRounded[Corner.TOP_RIGHT]) {
            mSquareCornersRect.set(right - radius, top, right, radius)
            canvas.drawRect(mSquareCornersRect, mBitmapPaint)
        }

        if (!mCornersRounded[Corner.BOTTOM_RIGHT]) {
            mSquareCornersRect.set(right - radius, bottom - radius, right, bottom)
            canvas.drawRect(mSquareCornersRect, mBitmapPaint)
        }

        if (!mCornersRounded[Corner.BOTTOM_LEFT]) {
            mSquareCornersRect.set(left, bottom - radius, left + radius, bottom)
            canvas.drawRect(mSquareCornersRect, mBitmapPaint)
        }
    }

    private fun redrawBorderForSquareCorners(canvas: Canvas) {
        if (all(mCornersRounded)) {
            // no square corners
            return
        }

        if (mCornerRadius == 0f) {
            return  // no round corners
        }

        val left = mDrawableRect.left
        val top = mDrawableRect.top
        val right = left + mDrawableRect.width()
        val bottom = top + mDrawableRect.height()
        val radius = mCornerRadius
        val offset = mBorderWidth / 2

        if (!mCornersRounded[Corner.TOP_LEFT]) {
            canvas.drawLine(left - offset, top, left + radius, top, mBorderPaint)
            canvas.drawLine(left, top - offset, left, top + radius, mBorderPaint)
        }

        if (!mCornersRounded[Corner.TOP_RIGHT]) {
            canvas.drawLine(right - radius - offset, top, right, top, mBorderPaint)
            canvas.drawLine(right, top - offset, right, top + radius, mBorderPaint)
        }

        if (!mCornersRounded[Corner.BOTTOM_RIGHT]) {
            canvas.drawLine(right - radius - offset, bottom, right + offset, bottom, mBorderPaint)
            canvas.drawLine(right, bottom - radius, right, bottom, mBorderPaint)
        }

        if (!mCornersRounded[Corner.BOTTOM_LEFT]) {
            canvas.drawLine(left - offset, bottom, left + radius, bottom, mBorderPaint)
            canvas.drawLine(left, bottom - radius, left, bottom, mBorderPaint)
        }
    }


    @Deprecated("Deprecated in Java")
    override fun getOpacity() = PixelFormat.TRANSLUCENT
    override fun getAlpha(): Int {
        return mBitmapPaint.getAlpha()
    }

    override fun setAlpha(alpha: Int) {
        mBitmapPaint.alpha = alpha
        invalidateSelf()
    }

    override fun getColorFilter(): ColorFilter? {
        return mBitmapPaint.colorFilter
    }

    override fun setColorFilter(cf: ColorFilter?) {
        mBitmapPaint.colorFilter = cf
        invalidateSelf()
    }

    @Deprecated("Deprecated in Java")
    override fun setDither(dither: Boolean) {
        mBitmapPaint.isDither = dither
        invalidateSelf()
    }

    override fun setFilterBitmap(filter: Boolean) {
        mBitmapPaint.isFilterBitmap = filter
        invalidateSelf()
    }

    override fun getIntrinsicWidth(): Int {
        return mBitmapWidth
    }

    override fun getIntrinsicHeight(): Int {
        return mBitmapHeight
    }

    /**
     * @return the corner radius.
     */
    fun getCornerRadius(): Float {
        return mCornerRadius
    }

    /**
     * @param corner the specific corner to get radius of.
     * @return the corner radius of the specified corner.
     */
    fun getCornerRadius(@Corner corner: Int): Float {
        return if (mCornersRounded[corner]) mCornerRadius else 0f
    }

    /**
     * Sets all corners to the specified radius.
     *
     * @param radius the radius.
     * @return the [RoundDrawable] for chaining.
     */
    fun setCornerRadius(radius: Float): RoundDrawable {
        setCornerRadius(radius, radius, radius, radius)
        return this
    }

    /**
     * Sets the corner radius of one specific corner.
     *
     * @param corner the corner.
     * @param radius the radius.
     * @return the [RoundDrawable] for chaining.
     */
    fun setCornerRadius(@Corner corner: Int, radius: Float): RoundDrawable {
        require(!(radius != 0f && mCornerRadius != 0f && mCornerRadius != radius)) { "Multiple nonzero corner radii not yet supported." }

        if (radius == 0f) {
            if (only(corner, mCornersRounded)) {
                mCornerRadius = 0f
            }
            mCornersRounded[corner] = false
        } else {
            if (mCornerRadius == 0f) {
                mCornerRadius = radius
            }
            mCornersRounded[corner] = true
        }

        return this
    }

    /**
     * Sets the corner radii of all the corners.
     *
     * @param topLeft     top left corner radius.
     * @param topRight    top right corner radius
     * @param bottomRight bototm right corner radius.
     * @param bottomLeft  bottom left corner radius.
     * @return the [RoundDrawable] for chaining.
     */
    fun setCornerRadius(
        topLeft: Float, topRight: Float, bottomRight: Float, bottomLeft: Float
    ): RoundDrawable {
        val radiusSet: MutableSet<Float?> = HashSet<Float?>(4)
        radiusSet.add(topLeft)
        radiusSet.add(topRight)
        radiusSet.add(bottomRight)
        radiusSet.add(bottomLeft)

        radiusSet.remove(0f)

        require(radiusSet.size <= 1) { "Multiple nonzero corner radii not yet supported." }

        if (!radiusSet.isEmpty()) {
            val radius: Float = radiusSet.iterator().next()!!
            require(!(isInfinite(radius) || isNaN(radius) || radius < 0)) { "Invalid radius value: " + radius }
            mCornerRadius = radius
        } else {
            mCornerRadius = 0f
        }

        mCornersRounded[Corner.TOP_LEFT] = topLeft > 0
        mCornersRounded[Corner.TOP_RIGHT] = topRight > 0
        mCornersRounded[Corner.BOTTOM_RIGHT] = bottomRight > 0
        mCornersRounded[Corner.BOTTOM_LEFT] = bottomLeft > 0
        return this
    }

    fun getBorderWidth(): kotlin.Float {
        return mBorderWidth
    }

    fun setBorderWidth(width: kotlin.Float): RoundDrawable {
        mBorderWidth = width
        mBorderPaint.setStrokeWidth(mBorderWidth)
        return this
    }

    fun getBorderColor(): Int {
        return mBorderColor.getDefaultColor()
    }

    fun setBorderColor(@ColorInt color: Int): RoundDrawable {
        return setBorderColor(ColorStateList.valueOf(color))
    }

    fun getBorderColors(): ColorStateList {
        return mBorderColor
    }

    fun setBorderColor(colors: ColorStateList?): RoundDrawable {
        mBorderColor = colors ?: ColorStateList.valueOf(0)
        mBorderPaint.setColor(mBorderColor.getColorForState(state, DEFAULT_BORDER_COLOR))
        return this
    }

    fun isOval(): Boolean {
        return mOval
    }

    fun setOval(oval: Boolean): RoundDrawable {
        mOval = oval
        return this
    }

    fun getScaleType(): ImageView.ScaleType {
        return mScaleType!!
    }

    fun setScaleType(scaleType: ImageView.ScaleType?): RoundDrawable {
        var type = scaleType
        if (type == null) {
            type = ImageView.ScaleType.FIT_CENTER
        }
        if (mScaleType != type) {
            mScaleType = type
            updateShaderMatrix()
        }
        return this
    }

    fun getTileModeX(): Shader.TileMode {
        return mTileModeX
    }

    fun setTileModeX(tileModeX: Shader.TileMode): RoundDrawable {
        if (mTileModeX != tileModeX) {
            mTileModeX = tileModeX
            mRebuildShader = true
            invalidateSelf()
        }
        return this
    }

    fun getTileModeY(): Shader.TileMode {
        return mTileModeY
    }

    fun setTileModeY(tileModeY: Shader.TileMode): RoundDrawable {
        if (mTileModeY != tileModeY) {
            mTileModeY = tileModeY
            mRebuildShader = true
            invalidateSelf()
        }
        return this
    }

    fun toBitmap(): Bitmap? {
        return drawableToBitmap(this)
    }

    companion object {
        const val TAG: String = "RoundedDrawable"
        val DEFAULT_BORDER_COLOR: Int = Color.BLACK

        fun fromBitmap(bitmap: Bitmap?): RoundDrawable? {
            if (bitmap != null) {
                return RoundDrawable(bitmap)
            } else {
                return null
            }
        }

        fun fromDrawable(drawable: Drawable?): Drawable? {
            if (drawable != null) {
                if (drawable is RoundDrawable) {
                    // just return if it's already a RoundedDrawable
                    return drawable
                } else if (drawable is LayerDrawable) {
                    val ld = drawable
                    val num = ld.numberOfLayers

                    // loop through layers to and change to RoundedDrawables if possible
                    for (i in 0..<num) {
                        val d = ld.getDrawable(i)
                        ld.setDrawableByLayerId(ld.getId(i), fromDrawable(d))
                    }
                    return ld
                }

                // try to get a bitmap from the drawable and
                val bm = drawableToBitmap(drawable)
                if (bm != null) {
                    return RoundDrawable(bm)
                }
            }
            return drawable
        }

        @SuppressLint("UseKtx")
        fun drawableToBitmap(drawable: Drawable): Bitmap? {
            if (drawable is BitmapDrawable) {
                return drawable.bitmap
            }

            var bitmap: Bitmap?
            val width = max(drawable.intrinsicWidth.toDouble(), 2.0).toInt()
            val height = max(drawable.intrinsicHeight.toDouble(), 2.0).toInt()
            try {
                bitmap = createBitmap(width, height)
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
            } catch (e: Throwable) {
                e.printStackTrace()
                bitmap = null
            }

            return bitmap
        }

        private fun only(index: Int, booleans: BooleanArray): Boolean {
            var i = 0
            val len = booleans.size
            while (i < len) {
                if (booleans[i] != (i == index)) {
                    return false
                }
                i++
            }
            return true
        }

        private fun any(booleans: BooleanArray): Boolean {
            for (b in booleans) {
                if (b) return true
            }
            return false
        }

        private fun all(booleans: BooleanArray): Boolean {
            for (b in booleans) {
                if (b) return false
            }
            return true
        }
    }
}