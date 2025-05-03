package com.wanwuzhinan.mingchang.ui.pop

import android.app.Activity
import com.colin.library.android.utils.ext.onClick
import com.ssm.comm.ext.setOnClickNoRepeat
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.databinding.PopExchangeCourseBinding


class ExchangeCoursePop(var context: Activity) :BasePop<PopExchangeCourseBinding>(context){

    override fun initClick() {

//        mDataBinding.tvContent.text = getConfigData().qrcode_desc

        onClick(mDataBinding.ivCancel){
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