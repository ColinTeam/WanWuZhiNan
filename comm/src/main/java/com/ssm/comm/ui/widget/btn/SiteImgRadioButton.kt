package com.ssm.comm.ui.widget.btn

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton
import com.ssm.comm.R

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.ui.widget.btn
 * ClassName: SiteImgRadioButton
 * Author:ShiMing Shi
 * CreateDate: 2022/9/6 12:33
 * Email:shiming024@163.com
 * Description:
 */
class SiteImgRadioButton : AppCompatRadioButton{

    // xml文件中设置的大小
    private var mDrawableSize = 0

    constructor(mContext: Context) : this(mContext,null)

    constructor(mContext: Context,mAttributeSet: AttributeSet?) : this(mContext,mAttributeSet,0)

    constructor(mContext: Context,mAttributeSet: AttributeSet?,mDefStyleAttr: Int) : super(mContext,mAttributeSet,mDefStyleAttr){
        initViews(context,mAttributeSet)
    }

    private fun initViews(context: Context,attrs: AttributeSet?){
        var mDrawableLeft: Drawable? = null
        var mDrawableTop: Drawable? = null
        var mDrawableRight: Drawable? = null
        var mDrawableBottom: Drawable? = null
        this.isClickable = true
        val mTypedArray = context.obtainStyledAttributes(attrs,R.styleable.SiteImgRadioButton)
        val n = mTypedArray.indexCount
        for(i in 0 until n){
            when (val attr = mTypedArray.getIndex(i)) {
                R.styleable.SiteImgRadioButton_drawableSize -> {
                    mDrawableSize = mTypedArray.getDimensionPixelSize(R.styleable.SiteImgRadioButton_drawableSize,50)
                }
                R.styleable.SiteImgRadioButton_drawableTop -> {
                    mDrawableTop = mTypedArray.getDrawable(attr)
                }
                R.styleable.SiteImgRadioButton_drawableBottom -> {
                    mDrawableRight = mTypedArray.getDrawable(attr)
                }
                R.styleable.SiteImgRadioButton_drawableRight -> {
                    mDrawableBottom = mTypedArray.getDrawable(attr)
                }
                R.styleable.SiteImgRadioButton_drawableLeft -> {
                    mDrawableLeft = mTypedArray.getDrawable(attr)
                }
            }
        }
        mTypedArray.recycle()
        setCompoundDrawablesWithIntrinsicBounds(mDrawableLeft,mDrawableTop,mDrawableRight,mDrawableBottom)
    }


    override fun setCompoundDrawablesWithIntrinsicBounds(
        left: Drawable?,
        top: Drawable?,
        right: Drawable?,
        bottom: Drawable?
    ) {
        left?.setBounds(0, 0, mDrawableSize, mDrawableSize)
        top?.setBounds(0, 0, mDrawableSize, mDrawableSize)

        right?.setBounds(0, 0, mDrawableSize, mDrawableSize)
        bottom?.setBounds(0, 0, mDrawableSize, mDrawableSize)

        setCompoundDrawables(left, top, right, bottom)
    }


}