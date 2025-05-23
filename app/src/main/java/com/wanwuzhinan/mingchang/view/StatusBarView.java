package com.wanwuzhinan.mingchang.view;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class StatusBarView extends View {
    public int mStatusBarHeight;

    public StatusBarView(Context context) {
        this(context, null);
    }

    public StatusBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mStatusBarHeight = getStatusBarHeight(context);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mStatusBarHeight, MeasureSpec.getSize(heightMeasureSpec));
    }



    //此处代码可以放到StatusBarUtils
    public  int getStatusBarHeight(Context context) {
        if (mStatusBarHeight == 0) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                mStatusBarHeight = res.getDimensionPixelSize(resourceId);
            }
        }
        return mStatusBarHeight;
    }
}
