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
@IntDef(Direction.LEFT, Direction.CENTER,Direction.RIGHT)
@Retention(AnnotationRetention.SOURCE)
annotation class Direction{
    companion object{
        const val LEFT = 0
        const val CENTER = 1
        const val RIGHT = 2
    }
}

