package com.colin.library.android.widget.banner.indicator

import androidx.viewpager.widget.ViewPager
import com.colin.library.android.widget.banner.options.IndicatorOptions

/**
 * IIndicator
 */
interface IIndicator : ViewPager.OnPageChangeListener {

    fun notifyDataChanged()

    fun setIndicatorOptions(options: IndicatorOptions)
}
