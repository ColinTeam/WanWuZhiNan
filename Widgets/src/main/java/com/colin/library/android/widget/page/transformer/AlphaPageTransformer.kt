package com.colin.library.android.widget.page.transformer

import android.view.View


class AlphaPageTransformer constructor(minAlpha: Float): BasePageTransformer() {

    companion object{
        const val DEFAULT_MIN_ALPHA = 0.5f
    }

    private var mMinAlpha = DEFAULT_MIN_ALPHA

    init{
        this.mMinAlpha = minAlpha
    }

    override fun transformPage(view: View, position: Float) {
        view.scaleX = 0.999f //hack
        if (position < -1) { // [-Infinity,-1)
            view.alpha = mMinAlpha
        } else if (position <= 1) { // [-1,1]
            //[0，-1]
            if (position < 0) {
                //[1,min]
                val factor = mMinAlpha + (1 - mMinAlpha) * (1 + position)
                view.alpha = factor
            } else { //[1，0]
                //[min,1]
                val factor = mMinAlpha + (1 - mMinAlpha) * (1 - position)
                view.alpha = factor
            }
        } else { // (1,+Infinity]
            view.alpha = mMinAlpha
        }
    }
}