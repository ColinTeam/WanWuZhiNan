package com.wanwuzhinan.mingchang.ui.pop

import android.app.Activity
import android.text.TextUtils
import com.colin.library.android.utils.ext.onClick
import com.ssm.comm.ext.toastError
import com.wanwuzhinan.mingchang.R


class ReportPop(var context: Activity) :BasePop<com.wanwuzhinan.mingchang.databinding.PopEditReasonBinding>(context){

    override fun initClick() {
        onClick(mDataBinding.ivCancel){
            dismiss()
        }
    }

    fun showPop(onSure: (name:String) -> Unit) {
        showPop()
        onClick(mDataBinding.tvSure){
            if(TextUtils.isEmpty(mDataBinding.edName.text.toString())){
                toastError(mDataBinding.edName.hint.toString())
                return@onClick
            }
            onSure(mDataBinding.edName.text.toString())
            dismiss()
        }
    }
    override fun getLayoutID(): Int {
        return R.layout.pop_edit_reason
    }
}