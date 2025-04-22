package com.ad.img_load.img;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Company:HD
 * ProjectName: 原力数藏
 * Package: com.nft.ylsc.ui.widget.img
 * ClassName: Corner
 * Author:ShiMing Shi
 * CreateDate: 2022/6/29 10:26
 * Email:shiming024@163.com
 * Description:
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({
        Corner.TOP_LEFT, Corner.TOP_RIGHT,
        Corner.BOTTOM_LEFT, Corner.BOTTOM_RIGHT
})
public @interface Corner {

    int TOP_LEFT = 0;
    int TOP_RIGHT = 1;
    int BOTTOM_RIGHT = 2;
    int BOTTOM_LEFT = 3;
}
