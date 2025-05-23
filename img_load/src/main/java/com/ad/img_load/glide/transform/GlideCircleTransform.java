package com.ad.img_load.glide.transform;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

public class GlideCircleTransform extends BitmapTransformation {

    private Paint mBorderPaint;
    private final float borderWidth;
    private final int borderColor;

    public GlideCircleTransform(){
        this(0f,Color.TRANSPARENT);
    }

    public GlideCircleTransform(int borderColor){
        this(10f,borderColor);
    }

    public GlideCircleTransform(float borderWidth, int borderColor){
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
        initBorderPaint();
    }

    private void initBorderPaint(){
        mBorderPaint = new Paint();
        mBorderPaint.setColor(borderColor);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(borderWidth);
        mBorderPaint.setDither(true);
    }


    @Override
    protected Bitmap transform(@NonNull BitmapPool pool,@NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool,toTransform);
    }


    private Bitmap circleCrop(BitmapPool pool, Bitmap source){
        if (source == null){
            return null;
        }
        //获取资源的长宽,获取最小值 子位图的像素个数
        int size = Math.min(source.getWidth(),source.getHeight());
        // 子位图第一个像素在源位图的X坐标
        int x = (source.getWidth() - size) / 2;
        //子位图第一个像素在源位图的y坐标
        int y = (source.getHeight() - size) / 2;
        //创建新位图  source 源位图
        Bitmap squared = Bitmap.createBitmap(source,x,y,size,size);
        //返回一个正好匹配给定宽、高和配置的只包含透明像素的Bitmap
        // 如果BitmapPool中找不到这样的Bitmap，就返回null
        Bitmap result = pool.get(size,size,Bitmap.Config.ARGB_8888);
        //当返回null 时,创建给定宽、高和配置的新位图
        if (result == null){
            result = Bitmap.createBitmap(size,size,Bitmap.Config.ARGB_8888);
        }
        //画图
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        // 设置shader
        paint.setShader(new BitmapShader(squared,BitmapShader.TileMode.CLAMP,BitmapShader.TileMode.CLAMP));
        //抗锯齿
        paint.setAntiAlias(true);
        float r = size / 2f;
        // 用设置好的画笔绘制一个圆
        canvas.drawCircle(r,r,r,paint);
        //描边
        float borderRadius = r - (borderWidth / 2);
        //画边框
        canvas.drawCircle(r, r, borderRadius, mBorderPaint);
        return result;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
