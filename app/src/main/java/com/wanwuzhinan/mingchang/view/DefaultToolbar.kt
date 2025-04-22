package com.wanwuzhinan.mingchang.view

import android.content.Context
import android.content.res.ColorStateList
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.databinding.DefaultToolbarBinding
import com.wanwuzhinan.mingchang.ext.visible
import com.ssm.comm.ext.setOnClickNoRepeat

class DefaultToolbar : ConstraintLayout {
    private var centerTitle: String? = null
    private var rightTitle: String? = null
    private var buRightTitle: String? = ""
    private var backVisibility: Boolean=true
    private var backColor=0
    private var rightImage: Int=0
    private var centerColor: Int=context.resources.getColor(R.color.white)
    private var rightColor: Int=context.resources.getColor(R.color.white)
    private var mDataBinding: DefaultToolbarBinding? = null
    private var leftImage: Int=0
    constructor(context: Context) : super(context) {
        initAttrs(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttrs(context, attrs)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.default_toolbar)
        centerTitle = typedArray.getString(R.styleable.default_toolbar_center_title)
        rightTitle = typedArray.getString(R.styleable.default_toolbar_dt_right_title)
        buRightTitle = typedArray.getString(R.styleable.default_toolbar_dt_bu_right_title)
        backVisibility = typedArray.getBoolean(R.styleable.default_toolbar_dt_back_visibility,true)
        backColor = typedArray.getColor(R.styleable.default_toolbar_dt_back_color,context.resources.getColor(R.color.white))
        rightImage = typedArray.getResourceId(R.styleable.default_toolbar_dt_right_image,0)
        leftImage = typedArray.getResourceId(R.styleable.default_toolbar_dt_left_image,
            R.mipmap.ic_back_mian)
        centerColor = typedArray.getColor(R.styleable.default_toolbar_dt_center_title_color,context.resources.getColor(
            R.color.white))
        rightColor = typedArray.getColor(R.styleable.default_toolbar_dt_right_title_color,context.resources.getColor(
            R.color.white))
        typedArray.recycle()
        init(context)
    }

    fun setCenterTitle(title:String){
        mDataBinding!!.tvCenter.text=title
    }

    fun getRightImage(): AppCompatImageView {
        return mDataBinding!!.ivRight
    }

    fun getRightText():TextView{
        return mDataBinding!!.tvRight
    }

    fun getRightButton():TextView{
        return mDataBinding!!.buRight
    }

    fun getLeftBack():AppCompatImageView{
        return mDataBinding!!.ivBack
    }

    private fun init(context: Context) {
        mDataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.default_toolbar, this, true)
        mDataBinding!!.tvCenter.text = centerTitle
        mDataBinding!!.tvRight.text=rightTitle
        mDataBinding!!.buRight.text=buRightTitle
        mDataBinding!!.tvCenter.setTextColor(centerColor)
        mDataBinding!!.tvRight.setTextColor(rightColor)
        mDataBinding!!.ivBack.setImageResource(leftImage)
        mDataBinding!!.ivBack.visible(backVisibility)
//        val colorStateList = ColorStateList.valueOf(backColor)
//        mDataBinding!!.ivBack.imageTintList=colorStateList

        mDataBinding!!.buRight.visibility=if(TextUtils.isEmpty(buRightTitle)) View.GONE else View.VISIBLE
        if(rightImage!=0){
            mDataBinding!!.ivRight.setImageResource(rightImage)
        }

        setOnClickNoRepeat(mDataBinding!!.ivBack) {
            if (context is AppCompatActivity) {
                val activity: AppCompatActivity = context
                activity.finish()
            }
        }
    }
}