package com.colin.library.android.widget.image

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.drawable.toDrawable
import com.colin.library.android.utils.Constants
import com.colin.library.android.widget.R
import com.colin.library.android.widget.def.Corner
import kotlin.math.max

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-01 22:26
 *
 * Des   :RoundedImageView
 */
class RoundImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    private var mType = 0
    private val mCornerRadii: FloatArray? =
        floatArrayOf(DEFAULT_RADIUS, DEFAULT_RADIUS, DEFAULT_RADIUS, DEFAULT_RADIUS)

    private var mBackgroundDrawable: Drawable? = null
    private var mBorderColor: ColorStateList? =
        ColorStateList.valueOf(RoundDrawable.DEFAULT_BORDER_COLOR)
    private var mBorderWidth = DEFAULT_BORDER_WIDTH
    private var mColorFilter: ColorFilter? = null
    private var mColorMod = false
    private var mDrawable: Drawable? = null
    private var mHasColorFilter = false
    private var mIsOval = false
    private var mMutateBackground = false
    private var mResource = 0
    private var mBackgroundResource = 0
    private var mScaleType: ScaleType? = null
    private var mTileModeX: Shader.TileMode? = DEFAULT_TILE_MODE
    private var mTileModeY: Shader.TileMode? = DEFAULT_TILE_MODE

    init {
        context.withStyledAttributes(attrs, R.styleable.RoundImageView, defStyleAttr, 0) {

            val index = getInt(R.styleable.RoundImageView_android_scaleType, Constants.INVALID)
            if (index >= 0) setScaleType(SCALE_TYPES[index])
            else setScaleType(ScaleType.FIT_CENTER)

            mType = getInt(R.styleable.RoundImageView_round_filter, 0)
            var cornerRadiusOverride = getDimensionPixelSize(
                R.styleable.RoundImageView_round_radius, Constants.INVALID
            ).toFloat()
            mCornerRadii!![Corner.TOP_LEFT] = getDimensionPixelSize(
                R.styleable.RoundImageView_round_radius_top_left, Constants.INVALID
            ).toFloat()
            mCornerRadii[Corner.TOP_RIGHT] = getDimensionPixelSize(
                R.styleable.RoundImageView_round_radius_top_right, Constants.INVALID
            ).toFloat()
            mCornerRadii[Corner.BOTTOM_RIGHT] = getDimensionPixelSize(
                R.styleable.RoundImageView_round_radius_bottom_right, Constants.INVALID
            ).toFloat()
            mCornerRadii[Corner.BOTTOM_LEFT] = getDimensionPixelSize(
                R.styleable.RoundImageView_round_radius_bottom_left, Constants.INVALID
            ).toFloat()

            var any = false
            var i = 0
            val len = mCornerRadii.size
            while (i < len) {
                if (mCornerRadii[i] < 0) mCornerRadii[i] = 0f
                else any = true
                i++
            }

            if (!any) {
                if (cornerRadiusOverride < 0) cornerRadiusOverride = DEFAULT_RADIUS
                var j = 0
                val size = mCornerRadii.size
                while (j < size) {
                    mCornerRadii[j] = cornerRadiusOverride
                    j++
                }
            }

            mBorderWidth =
                getDimensionPixelSize(R.styleable.RoundImageView_round_border_width, -1).toFloat()
            if (mBorderWidth < 0) {
                mBorderWidth = DEFAULT_BORDER_WIDTH
            }

            mBorderColor = getColorStateList(R.styleable.RoundImageView_round_border_color)
            if (mBorderColor == null) {
                mBorderColor = ColorStateList.valueOf(RoundDrawable.DEFAULT_BORDER_COLOR)
            }

            mMutateBackground =
                getBoolean(R.styleable.RoundImageView_round_mutate_background, false)
            mIsOval = getBoolean(R.styleable.RoundImageView_round_oval, false)

            val tileMode = getInt(R.styleable.RoundImageView_round_tile_mode, TILE_MODE_UNDEFINED)
            if (tileMode != TILE_MODE_UNDEFINED) {
                setTileModeX(parseTileMode(tileMode))
                setTileModeY(parseTileMode(tileMode))
            }

            val tileModeX =
                getInt(R.styleable.RoundImageView_round_tile_mode_x, TILE_MODE_UNDEFINED)
            if (tileModeX != TILE_MODE_UNDEFINED) {
                setTileModeX(parseTileMode(tileModeX))
            }

            val tileModeY =
                getInt(R.styleable.RoundImageView_round_tile_mode_y, TILE_MODE_UNDEFINED)
            if (tileModeY != TILE_MODE_UNDEFINED) {
                setTileModeY(parseTileMode(tileModeY))
            }

            updateDrawableAttrs()
            updateBackgroundDrawableAttrs(true)

            if (mMutateBackground) {
                super.setBackgroundDrawable(mBackgroundDrawable)
            }

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> if (mType == 1) {
                setFilter()
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> if (mType == 1) {
                removeFilter()
            }
        }
        return super.onTouchEvent(event)
    }


    /**
     * 设置滤镜
     */
    private fun setFilter() {
        //先获取设置的src图片
        getDrawable() ?: background?.let {
            colorFilter = PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY)
        }
    }

    /**
     * 清除滤镜
     */
    private fun removeFilter() {
        getDrawable() ?: background?.clearColorFilter()
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        invalidate()
    }

    override fun getScaleType(): ScaleType? {
        return mScaleType
    }

    override fun setScaleType(scaleType: ScaleType) {
        checkNotNull(scaleType)

        if (mScaleType != scaleType) {
            mScaleType = scaleType

            when (scaleType) {
                ScaleType.CENTER, ScaleType.CENTER_CROP, ScaleType.CENTER_INSIDE, ScaleType.FIT_CENTER, ScaleType.FIT_START, ScaleType.FIT_END, ScaleType.FIT_XY -> super.setScaleType(
                    ScaleType.FIT_XY
                )

                else -> super.setScaleType(scaleType)
            }

            updateDrawableAttrs()
            updateBackgroundDrawableAttrs(false)
            invalidate()
        }
    }

    override fun setImageDrawable(drawable: Drawable?) {
        mResource = 0
        mDrawable = RoundDrawable.fromDrawable(drawable)
        updateDrawableAttrs()
        super.setImageDrawable(mDrawable)
    }

    override fun setImageBitmap(bm: Bitmap?) {
        mResource = 0
        mDrawable = RoundDrawable.fromBitmap(bm)
        updateDrawableAttrs()
        super.setImageDrawable(mDrawable)
    }

    override fun setImageResource(@DrawableRes resId: Int) {
        if (mResource != resId) {
            mResource = resId
            mDrawable = resolveResource()
            updateDrawableAttrs()
            super.setImageDrawable(mDrawable)
        }
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        setImageDrawable(getDrawable())
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun resolveResource(): Drawable? {

        val src = getResources() ?: return null
        var d: Drawable? = null

        if (mResource != 0) {
            try {
                d = src.getDrawable(mResource, context.theme)
            } catch (e: Exception) {
                // Don't try again.
                mResource = 0
            }
        }
        return RoundDrawable.fromDrawable(d)
    }

    fun setImgFilter(@FloatRange(from = 0.0, to = 1.0) value: Float) {
        val mColorMatrix = ColorMatrix()
        //0-1
        mColorMatrix.setSaturation(value)
        val filter = ColorMatrixColorFilter(mColorMatrix)
        this.setColorFilter(filter)
    }

    override fun setBackground(background: Drawable?) {
        setBackgroundDrawable(background)
    }

    override fun setBackgroundResource(@DrawableRes resId: Int) {
        if (mBackgroundResource != resId) {
            mBackgroundResource = resId
            mBackgroundDrawable = resolveBackgroundResource()
            setBackgroundDrawable(mBackgroundDrawable)
        }
    }

    @SuppressLint("UseKtx")
    override fun setBackgroundColor(color: Int) {
        mBackgroundDrawable = color.toDrawable()
        setBackgroundDrawable(mBackgroundDrawable)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun resolveBackgroundResource(): Drawable? {
        val src = getResources() ?: return null
        var d: Drawable? = null
        if (mBackgroundResource != 0) {
            try {
                d = src.getDrawable(mBackgroundResource, context.theme)
            } catch (e: Exception) {
                // Don't try again.
                mBackgroundResource = 0
            }
        }
        return RoundDrawable.fromDrawable(d)
    }

    private fun updateDrawableAttrs() {
        updateAttrs(mDrawable, mScaleType)
    }

    private fun updateBackgroundDrawableAttrs(convert: Boolean) {
        if (mMutateBackground) {
            if (convert) {
                mBackgroundDrawable = RoundDrawable.fromDrawable(mBackgroundDrawable)
            }
            updateAttrs(mBackgroundDrawable, ScaleType.FIT_XY)
        }
    }

    override fun setColorFilter(cf: ColorFilter?) {
        if (mColorFilter !== cf) {
            mColorFilter = cf
            mHasColorFilter = true
            mColorMod = true
            applyColorMod()
            invalidate()
        }
    }

    private fun applyColorMod() {
        // Only mutate and apply when modifications have occurred. This should
        // not reset the mColorMod flag, since these filters need to be
        // re-applied if the Drawable is changed.
        if (mDrawable != null && mColorMod) {
            mDrawable = mDrawable!!.mutate()
            if (mHasColorFilter) {
                mDrawable!!.setColorFilter(mColorFilter)
            }
            // TODO: support, eventually...
            //mDrawable.setXfermode(mXfermode);
            //mDrawable.setAlpha(mAlpha * mViewAlphaScale >> 8);
        }
    }

    private fun updateAttrs(drawable: Drawable?, scaleType: ScaleType?) {
        if (drawable == null) {
            return
        }

        if (drawable is RoundDrawable) {
            drawable.setScaleType(scaleType).setBorderWidth(mBorderWidth)
                .setBorderColor(mBorderColor).setOval(mIsOval)
                .setTileModeX(mTileModeX ?: DEFAULT_TILE_MODE)
                .setTileModeY(mTileModeY ?: DEFAULT_TILE_MODE)

            if (mCornerRadii != null) {
                drawable.setCornerRadius(
                    mCornerRadii[Corner.TOP_LEFT],
                    mCornerRadii[Corner.TOP_RIGHT],
                    mCornerRadii[Corner.BOTTOM_RIGHT],
                    mCornerRadii[Corner.BOTTOM_LEFT]
                )
            }

            applyColorMod()
        } else if (drawable is LayerDrawable) {
            // loop through layers to and set drawable attrs
            val ld = drawable
            var i = 0
            val layers = ld.numberOfLayers
            while (i < layers) {
                updateAttrs(ld.getDrawable(i), scaleType)
                i++
            }
        }
    }

    override fun setBackgroundDrawable(background: Drawable?) {
        mBackgroundDrawable = background
        updateBackgroundDrawableAttrs(true)
        super.setBackgroundDrawable(mBackgroundDrawable)
    }

    /**
     * @return the largest corner radius.
     */
    fun getCornerRadius(): Float {
        return getMaxCornerRadius()
    }

    /**
     * @return the largest corner radius.
     */
    fun getMaxCornerRadius(): Float {
        var maxRadius = 0f
        for (r in mCornerRadii!!) {
            maxRadius = max(r.toDouble(), maxRadius.toDouble()).toFloat()
        }
        return maxRadius
    }

    /**
     * Get the corner radius of a specified corner.
     *
     * @param corner the corner.
     * @return the radius.
     */
    fun getCornerRadius(@Corner corner: Int): Float {
        return mCornerRadii!![corner]
    }

    /**
     * Set all the corner radii from a dimension resource id.
     *
     * @param resId dimension resource id of radii.
     */
    fun setCornerRadiusDimen(@DimenRes resId: Int) {
        val radius = resources.getDimension(resId)
        setCornerRadius(radius, radius, radius, radius)
    }

    /**
     * Set the corner radius of a specific corner from a dimension resource id.
     *
     * @param corner the corner to set.
     * @param resId  the dimension resource id of the corner radius.
     */
    fun setCornerRadiusDimen(@Corner corner: Int, @DimenRes resId: Int) {
        setCornerRadius(corner, resources.getDimensionPixelSize(resId).toFloat())
    }

    /**
     * Set the corner radii of all corners in px.
     *
     * @param radius the radius to set.
     */
    fun setCornerRadius(radius: Float) {
        setCornerRadius(radius, radius, radius, radius)
    }

    /**
     * Set the corner radius of a specific corner in px.
     *
     * @param corner the corner to set.
     * @param radius the corner radius to set in px.
     */
    fun setCornerRadius(@Corner corner: Int, radius: Float) {
        if (mCornerRadii!![corner] == radius) {
            return
        }
        mCornerRadii[corner] = radius

        updateDrawableAttrs()
        updateBackgroundDrawableAttrs(false)
        invalidate()
    }

    /**
     * Set the corner radii of each corner individually. Currently only one unique nonzero value is
     * supported.
     *
     * @param topLeft     radius of the top left corner in px.
     * @param topRight    radius of the top right corner in px.
     * @param bottomRight radius of the bottom right corner in px.
     * @param bottomLeft  radius of the bottom left corner in px.
     */
    fun setCornerRadius(topLeft: Float, topRight: Float, bottomLeft: Float, bottomRight: Float) {
        if (mCornerRadii!![Corner.TOP_LEFT] == topLeft && mCornerRadii[Corner.TOP_RIGHT] == topRight && mCornerRadii[Corner.BOTTOM_RIGHT] == bottomRight && mCornerRadii[Corner.BOTTOM_LEFT] == bottomLeft) {
            return
        }

        mCornerRadii[Corner.TOP_LEFT] = topLeft
        mCornerRadii[Corner.TOP_RIGHT] = topRight
        mCornerRadii[Corner.BOTTOM_LEFT] = bottomLeft
        mCornerRadii[Corner.BOTTOM_RIGHT] = bottomRight

        updateDrawableAttrs()
        updateBackgroundDrawableAttrs(false)
        invalidate()
    }

    fun getBorderWidth(): Float {
        return mBorderWidth
    }

    fun setBorderWidth(@DimenRes resId: Int) {
        setBorderWidth(getResources().getDimension(resId))
    }

    fun setBorderWidth(width: Float) {
        if (mBorderWidth == width) {
            return
        }

        mBorderWidth = width
        updateDrawableAttrs()
        updateBackgroundDrawableAttrs(false)
        invalidate()
    }

    @ColorInt
    fun getBorderColor(): Int {
        return mBorderColor!!.getDefaultColor()
    }

    fun setBorderColor(@ColorInt color: Int) {
        setBorderColor(ColorStateList.valueOf(color))
    }

    fun getBorderColors(): ColorStateList? {
        return mBorderColor
    }

    fun setBorderColor(colors: ColorStateList?) {
        if (mBorderColor == colors) {
            return
        }

        mBorderColor = colors ?: ColorStateList.valueOf(RoundDrawable.DEFAULT_BORDER_COLOR)
        updateDrawableAttrs()
        updateBackgroundDrawableAttrs(false)
        if (mBorderWidth > 0) {
            invalidate()
        }
    }

    /**
     * Return true if this view should be oval and always set corner radii to half the height or
     * width.
     *
     * @return if this [RoundImageView] is set to oval.
     */
    fun isOval(): Boolean {
        return mIsOval
    }

    /**
     * Set if the drawable should ignore the corner radii set and always round the source to
     * exactly half the height or width.
     *
     * @param oval if this [RoundImageView] should be oval.
     */
    fun setOval(oval: Boolean) {
        mIsOval = oval
        updateDrawableAttrs()
        updateBackgroundDrawableAttrs(false)
        invalidate()
    }

    fun getTileModeX(): Shader.TileMode? {
        return mTileModeX
    }

    fun setTileModeX(tileModeX: Shader.TileMode?) {
        if (this.mTileModeX == tileModeX) {
            return
        }

        this.mTileModeX = tileModeX
        updateDrawableAttrs()
        updateBackgroundDrawableAttrs(false)
        invalidate()
    }

    fun getTileModeY(): Shader.TileMode? {
        return mTileModeY
    }

    fun setTileModeY(tileModeY: Shader.TileMode?) {
        if (this.mTileModeY == tileModeY) {
            return
        }

        this.mTileModeY = tileModeY
        updateDrawableAttrs()
        updateBackgroundDrawableAttrs(false)
        invalidate()
    }

    /**
     * If `true`, we will also round the background drawable according to the settings on this
     * ImageView.
     *
     * @return whether the background is mutated.
     */
    fun mutatesBackground(): Boolean {
        return mMutateBackground
    }

    /**
     * Set whether the [RoundImageView] should round the background drawable according to
     * the settings in addition to the source drawable.
     *
     * @param mutate true if this view should mutate the background drawable.
     */
    fun mutateBackground(mutate: Boolean) {
        if (mMutateBackground == mutate) {
            return
        }

        mMutateBackground = mutate
        updateBackgroundDrawableAttrs(true)
        invalidate()
    }

    companion object {
        // Constants for tile mode attributes
        private val TILE_MODE_UNDEFINED = -2
        private const val TILE_MODE_CLAMP = 0
        private const val TILE_MODE_REPEAT = 1
        private const val TILE_MODE_MIRROR = 2
        const val TAG: String = "RoundedImageView"
        const val DEFAULT_RADIUS: Float = 0f
        const val DEFAULT_BORDER_WIDTH: Float = 0f
        val DEFAULT_TILE_MODE: Shader.TileMode = Shader.TileMode.CLAMP
        private val SCALE_TYPES = arrayOf<ScaleType>(
            ScaleType.MATRIX,
            ScaleType.FIT_XY,
            ScaleType.FIT_START,
            ScaleType.FIT_CENTER,
            ScaleType.FIT_END,
            ScaleType.CENTER,
            ScaleType.CENTER_CROP,
            ScaleType.CENTER_INSIDE
        )

        private fun parseTileMode(tileMode: Int): Shader.TileMode? {
            return when (tileMode) {
                TILE_MODE_CLAMP -> Shader.TileMode.CLAMP
                TILE_MODE_REPEAT -> Shader.TileMode.REPEAT
                TILE_MODE_MIRROR -> Shader.TileMode.MIRROR
                else -> null
            }
        }
    }
}
