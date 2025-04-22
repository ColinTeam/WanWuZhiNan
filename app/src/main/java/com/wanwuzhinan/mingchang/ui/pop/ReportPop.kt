package com.wanwuzhinan.mingchang.ui.pop

import android.app.Activity
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ad.img_load.setOnClickNoRepeat
import com.ssm.comm.ext.toastError
import com.tencent.smtt.utils.m
import com.wanwuzhinan.mingchang.R


class ReportPop(var context: Activity) :BasePop<com.wanwuzhinan.mingchang.databinding.PopEditReasonBinding>(context){

    override fun initClick() {
        setOnClickNoRepeat(mDataBinding.ivCancel){
            dismiss()
        }
    }

    fun showPop(onSure: (name:String) -> Unit) {
        showPop()
        setOnClickNoRepeat(mDataBinding.tvSure){
            if(TextUtils.isEmpty(mDataBinding.edName.text.toString())){
                toastError(mDataBinding.edName.hint.toString())
                return@setOnClickNoRepeat
            }
            onSure(mDataBinding.edName.text.toString())
            dismiss()
        }
    }
    override fun getLayoutID(): Int {
        return R.layout.pop_edit_reason
    }
}