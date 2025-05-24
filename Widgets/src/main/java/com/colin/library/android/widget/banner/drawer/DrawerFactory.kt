package com.colin.library.android.widget.banner.drawer

import com.colin.library.android.widget.banner.def.IndicatorStyle
import com.colin.library.android.widget.banner.options.IndicatorOptions

/**
 * Indicator Drawer Factory.
 */
internal object DrawerFactory {
    fun createDrawer(indicatorOptions: IndicatorOptions): IDrawer {
        return when (indicatorOptions.indicatorStyle) {
            IndicatorStyle.DASH -> DashDrawer(indicatorOptions)
            IndicatorStyle.ROUND_RECT -> RoundRectDrawer(indicatorOptions)
            else -> CircleDrawer(indicatorOptions)
        }
    }
}
