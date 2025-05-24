package com.colin.library.android.widget.banner.manager

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.colin.library.android.widget.banner.controller.AttributeController
import com.colin.library.android.widget.banner.options.BannerOptions
import com.colin.library.android.widget.banner.transform.OverlapPageTransformer
import com.colin.library.android.widget.banner.transform.ScaleInTransformer

/**
 * Banner管理类
 */
class BannerManager {
    private var bannerOptions: BannerOptions? = null

    private var attributeController: AttributeController? = null

    private var compositePageTransformer: CompositePageTransformer? = null

    private var marginPageTransformer: MarginPageTransformer? = null

    private var mDefaultPageTransformer: ViewPager2.PageTransformer? = null

    fun initAttrs(context: Context?, attrs: AttributeSet?) {
        getAttributeController().init(context, attrs)
    }

    fun getBannerOptions(): BannerOptions {
        if (bannerOptions == null) {
            bannerOptions = BannerOptions()
        }
        return bannerOptions!!
    }

    fun getAttributeController(): AttributeController {
        if (attributeController == null) {
            attributeController = AttributeController(getBannerOptions())
        }
        return attributeController!!
    }

    fun getCompositePageTransformer(): CompositePageTransformer {
        if (compositePageTransformer == null) {
            compositePageTransformer = CompositePageTransformer()
        }
        return compositePageTransformer!!
    }

    fun addTransformer(transformer: ViewPager2.PageTransformer) {
        getCompositePageTransformer().addTransformer(transformer)
    }

    fun removeTransformer(transformer: ViewPager2.PageTransformer) {
        getCompositePageTransformer().removeTransformer(transformer)
    }

    fun removeMarginPageTransformer() {
        marginPageTransformer?.let {
            getCompositePageTransformer().removeTransformer(it)
        }
    }

    fun removeDefaultPageTransformer() {
        if (mDefaultPageTransformer != null) {
            getCompositePageTransformer().removeTransformer(mDefaultPageTransformer!!)
        }
    }

    fun setPageMargin(pageMargin: Int) {
        getBannerOptions().setPageMargin(pageMargin)
    }

    fun createMarginTransformer() {
        removeMarginPageTransformer()
        marginPageTransformer = MarginPageTransformer(getBannerOptions().getPageMargin())
        getCompositePageTransformer().addTransformer(marginPageTransformer!!)
    }

    fun setMultiPageStyle(overlap: Boolean, scale: Float) {
        removeDefaultPageTransformer()
        mDefaultPageTransformer = if (overlap) {
            OverlapPageTransformer(getBannerOptions().getOrientation(), scale, 0f, 1f, 0f)
        } else {
            ScaleInTransformer(scale)
        }
        mDefaultPageTransformer?.let {
            getCompositePageTransformer().addTransformer(it)
        }
    }
}