package com.wanwuzhinan.mingchang.ui.pop

import android.app.Activity
import android.view.View
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.databinding.PopAnswerExplainBinding

class AnswerExplainPop(var context: Activity) :BasePop<PopAnswerExplainBinding>(context){

    fun showPop(view:View, desc:String) {
        mDataBinding.tvDesc.text=desc

        showUp(view)
    }

    override fun getLayoutID(): Int {
        return R.layout.pop_answer_explain
    }
}