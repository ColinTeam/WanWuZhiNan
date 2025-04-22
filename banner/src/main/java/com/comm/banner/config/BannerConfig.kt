package com.comm.banner.config

import com.comm.banner.util.BannerUtils

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.banner.config
 * ClassName: BannerConfig
 * Author:ShiMing Shi
 * CreateDate: 2022/10/8 15:29
 * Email:shiming024@163.com
 * Description:
 */
object BannerConfig {

    const val IS_AUTO_LOOP = true
    const val IS_INFINITE_LOOP = true
    const val LOOP_TIME = 3000
    const val SCROLL_TIME = 600
    const val INDICATOR_NORMAL_COLOR = -0x77000001
    const val INDICATOR_SELECTED_COLOR = -0x78000000

    val INDICATOR_NORMAL_WIDTH = BannerUtils.dp2px(5.0f).toInt()
    val INDICATOR_SELECTED_WIDTH = BannerUtils.dp2px(7.0f).toInt()
    val INDICATOR_SPACE = BannerUtils.dp2px(5.0f).toInt()
    val INDICATOR_MARGIN = BannerUtils.dp2px(5.0f).toInt()
    val INDICATOR_HEIGHT = BannerUtils.dp2px(3.0f).toInt()
    val INDICATOR_RADIUS = BannerUtils.dp2px(3.0f).toInt()
}