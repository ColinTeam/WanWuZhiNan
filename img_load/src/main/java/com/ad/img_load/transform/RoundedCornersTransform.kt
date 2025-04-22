package com.ad.img_load.transform

import android.content.res.Resources
import android.graphics.*
import android.util.TypedValue
import androidx.annotation.ColorInt
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import java.security.MessageDigest
import kotlin.math.roundToInt

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.ad.img_load.transform
 * ClassName: GlideRoundTransform
 * Author:ShiMing Shi
 * CreateDate: 2022/9/5 16:13
 * Email:shiming024@163.com
 * Description:矩形Transform
 */
class RoundedCornersTransform(borderWidth:Float? = .0f,@ColorInt borderColor: Int? = 0,radius:Float? = .0f) : BitmapTransformation(){

    private var mBorderPaint: Paint? = null
    private var borderWidth : Float?
    private var radius : Float? = .0f
    private var borderColor : Int = 0

    init{
        this.borderWidth = borderWidth
        this.radius = radius
        if (borderColor != null) {
            this.borderColor = borderColor
        }
        val displayMetrics = Resources.getSystem().displayMetrics
        this.radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radius!!, displayMetrics)
        mBorderPaint = Paint()
        if (borderColor != null) {
            if(borderColor > 0){
                mBorderPaint!!.color = borderColor
            }
        }
        if (borderWidth != null) {
            if(borderWidth > 0){
                mBorderPaint!!.strokeWidth = borderWidth
            }
        }
        mBorderPaint!!.style = Paint.Style.STROKE
        mBorderPaint!!.isDither = true
        mBorderPaint!!.isAntiAlias = true
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap? {
        val bitmap = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight)
        return roundCrop(pool,bitmap)
    }


    private fun roundCrop(pool: BitmapPool, source: Bitmap?): Bitmap? {
        if (source == null) return null
        val result = pool[source.width, source.height, Bitmap.Config.ARGB_8888]
        val canvas = Canvas(result)
        val paint = Paint()
        paint.shader =
            BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.isAntiAlias = true
        val rectF = RectF(0f, 0f, source.width.toFloat(), source.height.toFloat())
        canvas.drawRoundRect(rectF, radius!!, radius!!, paint)
        if (mBorderPaint != null) {
            canvas.drawRoundRect(rectF, radius!!, radius!!, mBorderPaint!!)
        }
        return result
    }

    fun getId(): String {
        return javaClass.name + (radius!!).roundToInt()
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {}
}