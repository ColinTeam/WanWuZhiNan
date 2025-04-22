package com.ssm.comm.ui.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ssm.comm.R
import com.ssm.comm.databinding.BaseToolbarBinding
import com.ssm.comm.ext.isEmpty
import com.ssm.comm.ext.setOnClickNoRepeat

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.ui.widget
 * ClassName: BaseToolBar
 * Author:ShiMing Shi
 * CreateDate: 2022/9/9 11:39
 * Email:shiming024@163.com
 * Description:标题栏
 */
class BaseToolBar : RelativeLayout {

    private var mDataBinding: BaseToolbarBinding? = null
    private var leftImgClickListener: OnClickListener? = null
    private var rightImgClickListener: OnClickListener? = null
    private var rightTextClickListener: OnClickListener? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initViews(context, attrs)
    }

    private fun initViews(context: Context, attrs: AttributeSet?) {
        mDataBinding =
            DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.base_toolbar, this, true)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseToolBar)
        val count = typedArray.indexCount
        for (i in 0 until count) {
            when (val index = typedArray.getIndex(i)) {
                R.styleable.BaseToolBar_leftImage -> {
                    mDataBinding!!.toolbarLeftImage.setImageDrawable(typedArray.getDrawable(index))
                }
                R.styleable.BaseToolBar_leftImageColor -> {
                    mDataBinding!!.toolbarLeftImage.imageTintList = ColorStateList.valueOf(
                        typedArray.getColor(
                            index,
                            Color.BLACK
                        )
                    )
                }
                R.styleable.BaseToolBar_leftImageVisible -> {
                    val visible = typedArray.getBoolean(index, true)
                    mDataBinding!!.toolbarLeftImage.visibility =
                        if (visible) View.VISIBLE else View.GONE
                }
                R.styleable.BaseToolBar_midText -> {
                    mDataBinding!!.toolbarTitleText.text = typedArray.getString(index)
                }
                R.styleable.BaseToolBar_midTextVisible -> {
                    val visible = typedArray.getBoolean(index, true)
                    mDataBinding!!.toolbarTitleText.visibility =
                        if (visible) View.VISIBLE else View.GONE
                }
                R.styleable.BaseToolBar_midTextFontSize -> {
                    mDataBinding!!.toolbarTitleText.textSize = typedArray.getDimensionPixelSize(
                        index,
                        resources.getDimension(R.dimen.sp_18).toInt()
                    ).toFloat()
                }
                R.styleable.BaseToolBar_midTextFontColor -> {
                    mDataBinding!!.toolbarTitleText.setTextColor(
                        typedArray.getColor(
                            index,
                            Color.WHITE
                        )
                    )
                }
                R.styleable.BaseToolBar_midTextAlpha -> {
                    mDataBinding!!.toolbarTitleText.alpha = typedArray.getFloat(index, 1.0f)
                }
                R.styleable.BaseToolBar_rightImage -> {
                    mDataBinding!!.toolbarRightImage.setImageDrawable(typedArray.getDrawable(index))
                }
                R.styleable.BaseToolBar_rightImageColor -> {
                    mDataBinding!!.toolbarRightImage.imageTintList = ColorStateList.valueOf(
                        typedArray.getColor(
                            index,
                            Color.BLACK
                        )
                    )
                }
                R.styleable.BaseToolBar_rightImageVisible -> {
                    val visible = typedArray.getBoolean(index, true)
                    mDataBinding!!.toolbarRightImage.visibility =
                        if (visible) View.VISIBLE else View.GONE
                }
                R.styleable.BaseToolBar_rightText -> {
                    mDataBinding!!.toolbarRightText.text = typedArray.getString(index)
                }
                R.styleable.BaseToolBar_rightTextFontSize -> {
                    mDataBinding!!.toolbarRightText.textSize = typedArray.getDimensionPixelSize(
                        index,
                        resources.getDimension(R.dimen.sp_14).toInt()
                    ).toFloat()
                }
                R.styleable.BaseToolBar_rightTextColor -> {
                    mDataBinding!!.toolbarRightText.setTextColor(
                        typedArray.getColor(
                            index,
                            Color.GRAY
                        )
                    )
                }
                R.styleable.BaseToolBar_rightTextVisible -> {
                    val visible = typedArray.getBoolean(index, true)
                    mDataBinding!!.toolbarRightText.visibility =
                        if (visible) View.VISIBLE else View.GONE
                }
                R.styleable.BaseToolBar_titleBarBackground -> {
                    val titleBarBackground = typedArray.getColor(index, -1)
                    if (titleBarBackground <= 0){
                        mDataBinding!!.toolbarLayout.setBackgroundResource(0)
                    }else{
                        mDataBinding!!.toolbarLayout.setBackgroundColor(titleBarBackground)
                    }
                }
            }
        }

        typedArray.recycle()

        setOnClickNoRepeat(
            mDataBinding!!.toolbarLeftImage,
            mDataBinding!!.toolbarRightText,
            mDataBinding!!.toolbarRightImage
        ) {
            when (it) {
                mDataBinding!!.toolbarLeftImage -> {
                    if (leftImgClickListener != null) {
                        leftImgClickListener!!.onClick(mDataBinding!!.toolbarLeftImage)
                    } else {
                        if (context is AppCompatActivity) {
                            val activity: AppCompatActivity = context
                            activity.finish()
                        }
                    }
                }
                mDataBinding!!.toolbarRightText -> {
                    if (rightTextClickListener != null) {
                        rightTextClickListener!!.onClick(mDataBinding!!.toolbarRightText)
                    }
                }
                mDataBinding!!.toolbarRightImage -> {
                    if (rightImgClickListener != null) {
                        rightImgClickListener!!.onClick(mDataBinding!!.toolbarRightImage)
                    }
                }
            }
        }
    }

    fun setLeftImgClickListener(listener: OnClickListener) = apply {
        this.leftImgClickListener = listener
    }

    fun setRightImgClickListener(listener: OnClickListener) = apply {
        this.rightImgClickListener = listener
    }

    fun setRightTextClickListener(listener: OnClickListener) = apply {
        this.rightTextClickListener = listener
    }

    fun setTitleText(title: String?) = apply {
        if (!isEmpty(title)) {
            mDataBinding!!.toolbarTitleText.text = title
        }
    }

    fun setTitleTextAlpha(alpha: Float = 1.0f) = apply {
        mDataBinding!!.toolbarTitleText.alpha = alpha
    }

    fun setTitleText(@StringRes id: Int) = apply {
        if (id != 0) {
            mDataBinding!!.toolbarTitleText.text = resources.getString(id)
        }
    }

    fun getTitleText(): String {
        return mDataBinding!!.toolbarTitleText.text.toString()
    }

    fun setRightTextVis(vis: Boolean) {
        mDataBinding!!.toolbarRightText.visibility = if (vis) View.VISIBLE else View.GONE
    }
}