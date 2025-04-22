package com.comm.banner.transformer

import androidx.viewpager2.widget.ViewPager2

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.banner.transformer
 * ClassName: BasePageTransformer
 * Author:ShiMing Shi
 * CreateDate: 2022/10/9 13:35
 * Email:shiming024@163.com
 * Description:
 */
abstract class BasePageTransformer : ViewPager2.PageTransformer {

    companion object {
        const val DEFAULT_CENTER = 0.5f
    }
}