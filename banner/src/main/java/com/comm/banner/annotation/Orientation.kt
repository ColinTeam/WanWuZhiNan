package com.comm.banner.annotation

import androidx.annotation.IntDef

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.banner.annotation
 * ClassName: Direction
 * Author:ShiMing Shi
 * CreateDate: 2022/10/8 15:38
 * Email:shiming024@163.com
 * Description:
 */
@IntDef(Orientation.HORIZONTAL, Orientation.VERTICAL)
@Retention(AnnotationRetention.SOURCE)
annotation class Orientation{
    companion object{
        const val HORIZONTAL = 0
        const val VERTICAL = 1
    }
}

