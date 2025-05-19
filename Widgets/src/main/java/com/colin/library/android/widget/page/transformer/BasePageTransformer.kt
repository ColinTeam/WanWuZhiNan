package com.colin.library.android.widget.page.transformer

import androidx.viewpager2.widget.ViewPager2

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025/5/19 10:18
 *
 * Des   :BasePageTransformer
 */
abstract class BasePageTransformer : ViewPager2.PageTransformer {

    companion object {
        const val DEFAULT_CENTER = 0.5f
    }
}