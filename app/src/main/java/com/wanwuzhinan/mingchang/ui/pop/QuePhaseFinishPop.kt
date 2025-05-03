package com.wanwuzhinan.mingchang.ui.pop

import android.app.Activity
import com.colin.library.android.utils.ext.onClick
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.databinding.PopQuestionPhaseFinishBinding

//确认框
class QuePhaseFinishPop(var context: Activity) :BasePop<PopQuestionPhaseFinishBinding>(context){

    fun showPop(pro:String,onSure: () -> Unit,onCancel: () -> Unit) {
        mDataBinding.tvPro.text= pro
        showPop()
        onClick(mDataBinding.tvSure,mDataBinding.tvCancel){
            dismiss()
            when(it){
                mDataBinding.tvSure ->{
                    onSure()
                }
                mDataBinding.tvCancel ->{
                    onCancel()
                }
            }
        }
    }

    override fun getLayoutID(): Int {
        return R.layout.pop_question_phase_finish
    }
}