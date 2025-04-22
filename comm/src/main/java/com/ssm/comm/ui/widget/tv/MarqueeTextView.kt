package com.ssm.comm.ui.widget.tv

import android.content.Context
import android.graphics.Rect
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.ui.widget.tv
 * ClassName: MarqueeTextView
 * Author:ShiMing Shi
 * CreateDate: 2022/9/7 19:34
 * Email:shiming024@163.com
 * Description:跑马灯
 */
class MarqueeTextView : AppCompatTextView{

    constructor(context: Context) : this(context,null)

    constructor(context: Context,attrs: AttributeSet?) : this(context,attrs,0)

    constructor(context: Context,attrs: AttributeSet?,defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        initView(context)
    }

    private fun initView(context: Context){
        this.ellipsize = TextUtils.TruncateAt.MARQUEE
        this.isSingleLine = true
        this.maxLines = 1
        this.marqueeRepeatLimit = -1
        this.requestFocus()
    }

    override fun isFocused(): Boolean {
        return true
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        if(focused){
            super.onFocusChanged(focused, direction, previouslyFocusedRect)
        }
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        if(hasWindowFocus){
            super.onWindowFocusChanged(hasWindowFocus)
        }
    }
}