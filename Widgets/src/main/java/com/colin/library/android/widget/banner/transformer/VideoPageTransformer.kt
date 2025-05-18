package com.colin.library.android.widget.banner.transformer

import android.view.View
import androidx.viewpager2.widget.ViewPager2

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-18 23:45
 *
 * Des   :VideoPageTransformer
 */
class VideoPageTransformer : ViewPager2.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        val pageWidth = page.width
        val pageHeight = page.height

        when {
            position < -1 || position > 1 -> { // 完全不在屏幕内的页面
                page.alpha = 0f
                page.scaleX = 0.8f // 缩小为 80%
                page.scaleY = 0.8f
            }
            position.toInt() == 0 -> { // 当前选中的页面
                page.alpha = 1f
                page.scaleX = 1f // 全屏展示
                page.scaleY = 1f
                page.translationX = 0f
            }
            else -> { // 在屏幕内的其他页面
                val scaleFactor = 0.8f // 卡片大小的缩放比例
                val translateValue = pageWidth * (1 - scaleFactor) * Math.abs(position)

                page.alpha = 1f
                page.scaleX = scaleFactor
                page.scaleY = scaleFactor
                page.translationX = -translateValue // 向右移动
            }
        }
    }
}

