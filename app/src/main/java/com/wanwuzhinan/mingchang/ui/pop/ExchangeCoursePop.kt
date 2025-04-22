package com.wanwuzhinan.mingchang.ui.pop

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.View.OnLongClickListener
import com.ad.img_load.setOnClickNoRepeat
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.databinding.PopExchangeCourseBinding
import com.wanwuzhinan.mingchang.ext.createQRCodeBitmap
import com.wanwuzhinan.mingchang.ext.getConfigData
import com.wanwuzhinan.mingchang.ext.launchWechatService


class ExchangeCoursePop(var context: Activity) :BasePop<PopExchangeCourseBinding>(context){

    override fun initClick() {

//        mDataBinding.tvContent.text = getConfigData().qrcode_desc

        setOnClickNoRepeat(mDataBinding.ivCancel){
            dismiss()
        }
    }

    fun showPop(onSure: () -> Unit,onContact: () -> Unit) {
        showHeightPop()
        setOnClickNoRepeat(mDataBinding.tvContact,mDataBinding.tvSure,mDataBinding.ivCancel){
            when(it){
                mDataBinding.tvSure ->{
                    dismiss()
                    onSure()
                }
                mDataBinding.tvContact ->{
                    dismiss()
                    onContact()
                }
                mDataBinding.ivCancel ->{
                    dismiss()
                }
            }

        }
    }

    override fun getLayoutID(): Int {
        return R.layout.pop_exchange_course
    }
}