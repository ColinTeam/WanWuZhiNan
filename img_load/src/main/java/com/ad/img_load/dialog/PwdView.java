package com.ad.img_load.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

@SuppressLint("AppCompatCustomView")
public class PwdView extends EditText {
    private static final String TAG = "PwdView";


    int width = getWidth();
    int height = getHeight();

    Paint linePaint;
    Paint circlePaint;

    public PwdView(Context context) {
        super(context);
        init();
    }

    public PwdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PwdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initPaints();
    }


    private void initPaints() {
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.parseColor("#dfdddf"));
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        linePaint.setStrokeWidth(4);

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.parseColor("#000000"));
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        circlePaint.setStrokeWidth(1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        width = getWidth();
        height = getHeight();

        for (int i = 1; i < 6; i++) //画五条竖线
            canvas.drawLine(width / 6 * i, 0, width / 6 * i, height, linePaint);

        int mid = width / 6 / 2;

        for (int i = 0; i < getText().toString().length(); i++) { //写了几个字符就画几个实心圆！
            canvas.drawCircle(width / 6 * i + mid, height / 2, height / 14 + 10, circlePaint);
        }

    }


    @Override
    public boolean isEnabled() {
        return false;
    }
}
