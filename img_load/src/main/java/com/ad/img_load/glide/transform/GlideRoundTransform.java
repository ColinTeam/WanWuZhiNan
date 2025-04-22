package com.ad.img_load.glide.transform;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import androidx.annotation.NonNull;

import com.ad.img_load.glide.enumx.CornerType;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.security.MessageDigest;

public class GlideRoundTransform extends CenterCrop {

    private final float mRadius;
    private final CornerType mCornerType;

    public GlideRoundTransform(float radius, CornerType type){
        this.mRadius = radius;
        this.mCornerType = type;
    }

    public GlideRoundTransform(CornerType type){
        this(0.0f,type);
    }

    public GlideRoundTransform(float radius){
        this(radius,CornerType.ALL);
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap bm = super.transform(pool,toTransform,outWidth,outHeight);
        return roundCrop(pool,bm);
    }

    private Bitmap roundCrop(BitmapPool pool, Bitmap source){
        if (source == null){
            return null;
        }
        int width = source.getWidth();
        int height = source.getHeight();
        Bitmap.Config config = Bitmap.Config.ARGB_4444;
        Bitmap result = pool.get(source.getWidth(),source.getHeight(),config);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source,BitmapShader.TileMode.CLAMP,BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        Path path = new Path();
        drawRoundRect(canvas,paint,path,width,height);
        return result;
    }


    private void drawRoundRect(Canvas canvas,Paint paint,Path path,int width,int height){
        float[] rids;
        switch (mCornerType){
            case ALL:
                rids = new float[]{mRadius,mRadius,mRadius,mRadius,mRadius,mRadius,mRadius,mRadius};
                drawPath(rids,canvas,paint,path,width,height);
                break;
            case TOP_LEFT:
                rids = new float[]{mRadius,mRadius,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f};
                drawPath(rids,canvas,paint,path,width,height);
                break;
            case TOP_RIGHT:
                rids  = new float[]{0.0f,0.0f,mRadius,mRadius,0.0f,0.0f,0.0f,0.0f};
                drawPath(rids,canvas,paint,path,width,height);
                break;
            case BOTTOM_RIGHT:
                rids  = new float[]{0.0f,0.0f,0.0f,0.0f,mRadius,mRadius,0.0f,0.0f};
                drawPath(rids,canvas,paint,path,width,height);
                break;
            case BOTTOM_LEFT:
                rids  = new float[]{0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,mRadius,mRadius};
                drawPath(rids,canvas,paint,path,width,height);
                break;
            case TOP:
                rids = new float[]{mRadius,mRadius,mRadius,mRadius,0.0f,0.0f,0.0f,0.0f};
                drawPath(rids,canvas,paint,path,width,height);
                break;
            case BOTTOM:
                rids  = new float[]{0.0f,0.0f,0.0f,0.0f,mRadius,mRadius,mRadius,mRadius};
                drawPath(rids,canvas,paint,path,width,height);
                break;
            case LEFT:
                rids = new float[]{mRadius,mRadius,0.0f,0.0f,0.0f,0.0f,mRadius,mRadius};
                drawPath(rids,canvas,paint,path,width,height);
                break;
            case RIGHT:
                rids  = new float[]{0.0f,0.0f,mRadius,mRadius,mRadius,mRadius,0.0f,0.0f};
                drawPath(rids,canvas,paint,path,width,height);
                break;
            case TOP_LEFT_BOTTOM_RIGHT:
                rids  = new float[]{mRadius,mRadius,0.0f,0.0f,mRadius,mRadius,0.0f,0.0f};
                drawPath(rids,canvas,paint,path,width,height);
                break;
            case TOP_RIGHT_BOTTOM_LEFT:
                rids  = new float[]{0.0f,0.0f,mRadius,mRadius,0.0f,0.0f,mRadius,mRadius};
                drawPath(rids,canvas,paint,path,width,height);
                break;
            case TOP_LEFT_TOP_RIGHT_BOTTOM_RIGHT:
                rids  = new float[]{mRadius,mRadius,mRadius,mRadius,mRadius,mRadius,0.0f,0.0f};
                drawPath(rids,canvas,paint,path,width,height);
                break;
            case TOP_RIGHT_BOTTOM_RIGHT_BOTTOM_LEFT:
                rids  = new float[]{0.0f,0.0f,mRadius,mRadius,mRadius,mRadius,mRadius,mRadius};
                drawPath(rids,canvas,paint,path,width,height);
                break;
            default:
                throw new RuntimeException("RoundedCorners type not belong to CornerType");
        }
    }

    //rids 圆角的半径，依次为左上角xy半径，右上角，右下角，左下角
    private void drawPath(float[] rids,Canvas canvas,Paint paint,Path path,int width,int height){
        path.addRoundRect(new RectF(0,0,width,height),rids,Path.Direction.CW);
        canvas.drawPath(path,paint);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
