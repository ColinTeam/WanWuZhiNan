package com.colin.library.android.widget.indicator

import android.view.View
import androidx.viewpager.widget.ViewPager.OnPageChangeListener

interface IIndicator : OnPageChangeListener {

    fun getIndicatorView(): View

    fun getIndicatorConfig(): IndicatorConfig

    fun onPageChanged(count: Int, current: Int)
}