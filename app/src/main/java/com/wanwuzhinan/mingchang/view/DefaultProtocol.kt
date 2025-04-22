package com.wanwuzhinan.mingchang.view

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.DefaultProtocolBinding
import com.wanwuzhinan.mingchang.ext.performLaunchH5Agreement
import com.wanwuzhinan.mingchang.ext.visible
import com.ssm.comm.ext.getStringExt
import com.ssm.comm.ext.setOnClickNoRepeat

class DefaultProtocol : ConstraintLayout {
    private var mTextColor: Int = 0
    private var mTitle1: String ?=""
    private var mTitle2: String ?=context.getString(R.string.privacy_policy)
    private var mTitle3: String ?= ""
    lateinit var mDataBinding: DefaultProtocolBinding

    var clickSelect : (isSelect:Boolean) -> Unit? = {

    }
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
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.default_protocol)
        mTextColor = typedArray.getColor(R.styleable.default_protocol_default_protocol_color,context.resources.getColor(R.color.color_default))
        mTitle1 = typedArray.getString(R.styleable.default_protocol_agreement_title1)
        mTitle2 = typedArray.getString(R.styleable.default_protocol_agreement_title2)
        mTitle3 = typedArray.getString(R.styleable.default_protocol_agreement_title3)
        typedArray.recycle()
        init(context)
    }

    public fun isSelect():Boolean{
        return mDataBinding.ivSelect.isSelected
    }

    private fun init(context: Context) {
        mDataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.default_protocol, this, true)
        mDataBinding.tvUserAgreement.setTextColor(mTextColor)
        mDataBinding.tvPrivacyAgreement.setTextColor(mTextColor)
        mDataBinding.tvChildAgreement.setTextColor(mTextColor)
        mDataBinding.tvUserAgreement.text=mTitle1
        mDataBinding.tvPrivacyAgreement.text=mTitle2
        mDataBinding.tvChildAgreement.text=mTitle3

//        mDataBinding.tvAnd.visible(!mTitle2.isNullOrEmpty())
        mDataBinding.tvPrivacyAgreement.visible(!mTitle2.isNullOrEmpty())

        setOnClickNoRepeat(mDataBinding.tvUserAgreement,mDataBinding.tvPrivacyAgreement,mDataBinding.tvChildAgreement,mDataBinding.ivSelect) {
            when (it) {
                mDataBinding.tvUserAgreement -> {
                    start(mTitle1!!)
                }

                mDataBinding.ivSelect -> {
                    mDataBinding.ivSelect.isSelected=!mDataBinding.ivSelect.isSelected
                    clickSelect.invoke(mDataBinding.ivSelect.isSelected)
                }

                mDataBinding.tvPrivacyAgreement -> {
                    start(mTitle2!!)
                }
                mDataBinding.tvChildAgreement-> {
                    start(mTitle3!!)
                }
            }
        }
    }

    private fun start(title:String){
        var activity=context as Activity

        when(title){
            getStringExt(R.string.user_privacy) ->{//用户协议
                activity.performLaunchH5Agreement(ConfigApp.USER_AGREEMENT,context.getString(R.string.user_privacy))
            }
            getStringExt(R.string.privacy_policy) ->{//隐私政策
                activity.performLaunchH5Agreement(ConfigApp.PRIVACY_POLICY,context.getString(R.string.privacy_policy))
            }
            getStringExt(R.string.privacy_child) ->{//隐私政策
                activity.performLaunchH5Agreement(ConfigApp.PRIVACY_CHILD,context.getString(R.string.privacy_child))
            }
        }
    }

    fun setClick(click: (isSelect:Boolean) -> Unit){
        clickSelect = click
    }
}