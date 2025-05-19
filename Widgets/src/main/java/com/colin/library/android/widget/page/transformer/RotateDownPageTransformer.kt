package com.colin.library.android.widget.page.transformer

import android.view.View

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.banner.transformer
 * ClassName: RotateDownPageTransformer
 * Author:ShiMing Shi
 * CreateDate: 2022/10/9 13:42
 * Email:shiming024@163.com
 * Description:
 */
class RotateDownPageTransformer constructor(maxRotate: Float): BasePageTransformer() {

    companion object{
        const val DEFAULT_MAX_ROTATE = 15.0f
    }

    private var mMaxRotate = DEFAULT_MAX_ROTATE

    init{
        this.mMaxRotate = maxRotate
    }

    override fun transformPage(view: View, position: Float) {
        if (position < -1) {
            // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.rotation = mMaxRotate * -1
            view.pivotX = view.width.toFloat()
            view.pivotY = view.height.toFloat()
        } else if (position <= 1) { // [-1,1]
            if (position < 0) { //[0，-1]
                view.pivotX = view.width * (DEFAULT_CENTER + DEFAULT_CENTER * -position)
                view.pivotY = view.height.toFloat()
                view.rotation = mMaxRotate * position
            } else { //[1,0]
                view.pivotX = view.width * DEFAULT_CENTER * (1 - position)
                view.pivotY = view.height.toFloat()
                view.rotation = mMaxRotate * position
            }
        } else {
            // (1,+Infinity]
            // This page is way off-screen to the right.
            view.rotation = mMaxRotate
            view.pivotX = (view.width * 0).toFloat()
            view.pivotY = view.height.toFloat()
        }
    }


}