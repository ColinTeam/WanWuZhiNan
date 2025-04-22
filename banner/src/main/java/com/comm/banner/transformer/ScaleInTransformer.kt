package com.comm.banner.transformer

import android.view.View

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.banner.transformer
 * ClassName: ScaleInTransformer
 * Author:ShiMing Shi
 * CreateDate: 2022/10/9 13:48
 * Email:shiming024@163.com
 * Description:
 */
class ScaleInTransformer constructor(minScale: Float): BasePageTransformer() {

    companion object{
        const val DEFAULT_MIN_SCALE = 0.85f
    }

    private var mMinScale = DEFAULT_MIN_SCALE

    init{
        this.mMinScale = minScale
    }

    override fun transformPage(view: View, position: Float) {
        val pageWidth = view.width
        val pageHeight = view.height
        view.pivotY = (pageHeight / 2).toFloat()
        view.pivotX = (pageWidth / 2).toFloat()
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.scaleX = mMinScale
            view.scaleY = mMinScale
            view.pivotX = pageWidth.toFloat()
        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            if (position < 0) //1-2:1[0,-1] ;2-1:1[-1,0]
            {
                val scaleFactor = (1 + position) * (1 - mMinScale) + mMinScale
                view.scaleX = scaleFactor
                view.scaleY = scaleFactor
                view.pivotX = pageWidth * (DEFAULT_CENTER + DEFAULT_CENTER * -position)
            } else  //1-2:2[1,0] ;2-1:2[0,1]
            {
                val scaleFactor = (1 - position) * (1 - mMinScale) + mMinScale
                view.scaleX = scaleFactor
                view.scaleY = scaleFactor
                view.pivotX = pageWidth * ((1 - position) * DEFAULT_CENTER)
            }
        } else { // (1,+Infinity]
            view.pivotX = 0f
            view.scaleX = mMinScale
            view.scaleY = mMinScale
        }
    }
}