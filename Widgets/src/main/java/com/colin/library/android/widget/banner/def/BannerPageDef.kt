package com.colin.library.android.widget.banner.def

import androidx.annotation.IntDef

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-24 12:18
 *
 * Des   :BannerPageDef
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@IntDef(PageStyle.NORMAL, PageStyle.MULTI_PAGE_OVERLAP, PageStyle.MULTI_PAGE_SCALE)
@Retention(AnnotationRetention.SOURCE)
annotation class PageStyle {
    companion object {
        const val NORMAL = 0
        const val MULTI_PAGE_OVERLAP = 1 shl 1
        const val MULTI_PAGE_SCALE = 1 shl 2
    }
}