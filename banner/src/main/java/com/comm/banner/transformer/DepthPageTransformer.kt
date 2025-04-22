package com.comm.banner.transformer

import android.view.View
import kotlin.math.abs

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.banner.transformer
 * ClassName: DepthPageTransformer
 * Author:ShiMing Shi
 * CreateDate: 2022/10/9 13:38
 * Email:shiming024@163.com
 * Description:
 */
class DepthPageTransformer constructor(minScale: Float): BasePageTransformer() {

    companion object{
        const val DEFAULT_MIN_SCALE = 0.75f
    }

    private var mMinScale = DEFAULT_MIN_SCALE

    init{
        this.mMinScale = minScale
    }

    override fun transformPage(view: View, position: Float) {
        val pageWidth: Int = view.width
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.alpha = 0f
        } else if (position <= 0) { // [-1,0]
            // Use the default slide transition when moving to the left page
            view.alpha = 1f
            view.translationX = 0f
            view.scaleX = 1f
            view.scaleY = 1f
        } else if (position <= 1) { // (0,1]
            //进入页面时
            view.visibility = View.VISIBLE
            // Fade the page out.
            view.alpha = 1 - position

            // Counteract the default slide transition
            view.translationX = pageWidth * -position

            // Scale the page down (between MIN_SCALE and 1)
            val scaleFactor = (mMinScale
                    + (1 - mMinScale) * (1 - abs(position)))
            view.scaleX = scaleFactor
            view.scaleY = scaleFactor
            //退出页面时
            if (position == 1f) {
                view.visibility = View.INVISIBLE
            }
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.alpha = 0f
        }
    }
}