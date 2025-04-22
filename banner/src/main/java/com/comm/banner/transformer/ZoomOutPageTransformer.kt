package com.comm.banner.transformer

import android.view.View

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.banner.transformer
 * ClassName: ZoomOutPageTransformer
 * Author:ShiMing Shi
 * CreateDate: 2022/10/9 13:49
 * Email:shiming024@163.com
 * Description:
 */
class ZoomOutPageTransformer constructor(minScale: Float = .0f,minAlpha: Float = .0f): BasePageTransformer() {

    companion object{
        const val DEFAULT_MIN_SCALE = 0.85f
        const val DEFAULT_MIN_ALPHA = 0.5f
    }

    private var mMinScale = DEFAULT_MIN_SCALE
    private var mMinAlpha = DEFAULT_MIN_ALPHA

    init{
        this.mMinScale = minScale
        this.mMinAlpha = minAlpha
    }


    override fun transformPage(view: View, position: Float) {
        val pageWidth = view.width
        val pageHeight = view.height
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.alpha = 0f
        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            val scaleFactor = Math.max(mMinScale, 1 - Math.abs(position))
            val vertMargin = pageHeight * (1 - scaleFactor) / 2
            val horzMargin = pageWidth * (1 - scaleFactor) / 2
            if (position < 0) {
                view.translationX = horzMargin - vertMargin / 2
            } else {
                view.translationX = -horzMargin + vertMargin / 2
            }

            // Scale the page down (between MIN_SCALE and 1)
            view.scaleX = scaleFactor
            view.scaleY = scaleFactor

            // Fade the page relative to its size.
            view.alpha = mMinAlpha +
                    (scaleFactor - mMinScale) /
                    (1 - mMinScale) * (1 - mMinAlpha)
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.alpha = 0f
        }
    }







}