package com.ad.img_load.img;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * Company:HD
 * ProjectName: 原力数藏
 * Package: com.nft.ylsc.ui.widget
 * ClassName: ResizableImageView
 * Author:ShiMing Shi
 * CreateDate: 2022/6/11 19:52
 * Email:shiming024@163.com
 * Description:
 */
public class ResizableImageView extends AppCompatImageView {

    private int value=0;

    public ResizableImageView(@NonNull Context context) {
        super(context);
    }

    public ResizableImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ResizableImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        Drawable d = getDrawable();
        if(d!=null){
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = width;
            //高度根据使得图片的宽度充满屏幕计算而得（这个是默认计算）
            //  int height = (int) Math.ceil((float) width * (float) d.getIntrinsicHeight() / (float) d.getIntrinsicWidth());
            if (value==1){
                height = (int) Math.ceil((float) (width*4/3));
            }
            setMeasuredDimension(width, height);
        }else{
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    //根据传过来的值，1是宽度3：4，其它值是1：1
    public void  setMeasure(int value){
        this.value= value;
    }
}
