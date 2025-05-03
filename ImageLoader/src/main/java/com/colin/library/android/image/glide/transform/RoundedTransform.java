package com.colin.library.android.image.glide.transform;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

public class RoundedTransform extends BitmapTransformation {

    private final float topLeft;
    private final float topRight;
    private final float bottomRight;
    private final float bottomLeft;

    public RoundedTransform(float topLeft, float topRight, float bottomRight, float bottomLeft) {
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomRight = bottomRight;
        this.bottomLeft = bottomLeft;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap result = Bitmap.createBitmap(toTransform.getWidth(), toTransform.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(new BitmapShader(toTransform, android.graphics.Shader.TileMode.CLAMP, android.graphics.Shader.TileMode.CLAMP));

        Path path = new Path();
        float[] radii = new float[]{
                topLeft, topLeft,     // Top-left
                topRight, topRight,   // Top-right
                bottomRight, bottomRight, // Bottom-right
                bottomLeft, bottomLeft    // Bottom-left
        };
        path.addRoundRect(new RectF(0f, 0f, toTransform.getWidth(), toTransform.getHeight()), radii, Path.Direction.CW);

        canvas.drawPath(path, paint);
        return result;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(("RoundedCornersTransformation" + topLeft + topRight + bottomRight + bottomLeft).getBytes());
    }
}