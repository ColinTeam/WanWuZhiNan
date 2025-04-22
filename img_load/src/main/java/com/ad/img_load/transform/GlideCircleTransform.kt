package com.ad.img_load.transform

import android.graphics.*
import androidx.annotation.ColorInt
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.ad.img_load.transform
 * ClassName: GlideCircleTransform
 * Author:ShiMing Shi
 * CreateDate: 2022/9/5 15:57
 * Email:shiming024@163.com
 * Description:圆形Transform
 */
class GlideCircleTransform(borderWidth:Float? = .0f,@ColorInt borderColor: Int? = 0) : BitmapTransformation(){

    private var mBorderPaint: Paint? = null
    private var borderWidth : Float? = .0f
    private var borderColor : Int? = null

    init{
        if(borderColor != null && borderColor > 0){
            this.borderColor = borderColor
            if(borderWidth != null && borderWidth > .0f){
                this.borderWidth = borderWidth
                mBorderPaint = Paint()
                mBorderPaint!!.color = borderColor
                mBorderPaint!!.strokeWidth = borderWidth
                mBorderPaint!!.style = Paint.Style.STROKE
                mBorderPaint!!.isDither = true
                mBorderPaint!!.isAntiAlias = true
            }
        }
    }


    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap? {
        return circleCrop(pool,toTransform)
    }

    private fun circleCrop(pool: BitmapPool, source: Bitmap?): Bitmap? {
        if (source == null) {
            return null
        }
        //获取资源的长宽,获取最小值 子位图的像素个数
        val size = source.width.coerceAtMost(source.height)
        // 子位图第一个像素在源位图的X坐标
        val x = (source.width - size) / 2
        //子位图第一个像素在源位图的y坐标
        val y = (source.height - size) / 2
        //创建新位图  source 源位图
        val squared = Bitmap.createBitmap(source, x, y, size, size)
        //返回一个正好匹配给定宽、高和配置的只包含透明像素的Bitmap
        // 如果BitmapPool中找不到这样的Bitmap，就返回null
        var result: Bitmap? = pool[size, size, Bitmap.Config.ARGB_8888]
        //当返回null 时,创建给定宽、高和配置的新位图
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        }
        //画图
        val canvas = Canvas(result!!)
        val paint = Paint()
        // 设置shader
        paint.shader =
            BitmapShader(squared, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        //抗锯齿
        paint.isAntiAlias = true
        val r = size / 2f
        // 用设置好的画笔绘制一个圆
        canvas.drawCircle(r, r, r, paint)
        if(mBorderPaint != null){
            //描边
            val borderRadius = r - borderWidth!! / 2
            //画边框
            canvas.drawCircle(r, r, borderRadius, mBorderPaint!!)
        }
        return result
    }


    override fun updateDiskCacheKey(messageDigest: MessageDigest) {

    }
}