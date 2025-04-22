package com.comm.banner.indicator

import android.view.View
import com.comm.banner.config.IndicatorConfig
import com.comm.banner.listener.OnPageChangeListener

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.banner.indicator
 * ClassName: Indicator
 * Author:ShiMing Shi
 * CreateDate: 2022/10/8 15:58
 * Email:shiming024@163.com
 * Description:
 */
interface Indicator: OnPageChangeListener {

    fun getIndicatorView(): View

    fun getIndicatorConfig(): IndicatorConfig

    fun onPageChanged(count: Int,currentPosition: Int)
}