package com.wanwuzhinan.mingchang.ui.pop

import android.app.Activity
import com.ad.img_load.setOnClickNoRepeat
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.databinding.PopQuestionFinishBinding
import com.wanwuzhinan.mingchang.databinding.PopQuestionPhaseFinishBinding
import com.wanwuzhinan.mingchang.databinding.PopSureBinding
import com.wanwuzhinan.mingchang.ext.visible

//确认框
class QuestionFinishPop(var context: Activity) :BasePop<PopQuestionFinishBinding>(context){

    fun showPop(pro: String, onSure: () -> Unit) {
        if (pro.toInt() > 30){
            mDataBinding.ivStar3.setImageResource(R.mipmap.ic_question_finish_alert_star_n)
            mDataBinding.ivStar2.setImageResource(R.mipmap.ic_question_finish_alert_star_n)
            mDataBinding.ivStar1.setImageResource(R.mipmap.ic_question_finish_alert_star_s)
        }
        if (pro.toInt() > 60){
            mDataBinding.ivStar3.setImageResource(R.mipmap.ic_question_finish_alert_star_n)
            mDataBinding.ivStar2.setImageResource(R.mipmap.ic_question_finish_alert_star_s)
            mDataBinding.ivStar1.setImageResource(R.mipmap.ic_question_finish_alert_star_s)
        }
        if (pro.toInt() > 90){
            mDataBinding.ivStar3.setImageResource(R.mipmap.ic_question_finish_alert_star_s)
            mDataBinding.ivStar2.setImageResource(R.mipmap.ic_question_finish_alert_star_s)
            mDataBinding.ivStar1.setImageResource(R.mipmap.ic_question_finish_alert_star_s)
        }
        showPop()
        setOnClickNoRepeat(mDataBinding.tvSure){
            dismiss()
            when(it){
                mDataBinding.tvSure ->{
                    onSure()
                }
            }
        }
    }

    override fun getLayoutID(): Int {
        return R.layout.pop_question_finish
    }
}