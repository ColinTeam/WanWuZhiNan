package com.wanwuzhinan.mingchang.adapter

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.QuestionListData


class AnswerPracticeOptionAdapter : BaseQuickAdapter<QuestionListData.AnswerBean,QuickViewHolder>() {

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: QuestionListData.AnswerBean?) {
        holder.setText(R.id.tv_option,"${item!!.answer}")

        when(position){
            0 ->{
                holder.setImageResource(R.id.iv_answer,if (item.answerType > 0) R.mipmap.ic_question_answer_a_s else R.mipmap.ic_question_answer_a)
            }
            1 ->{
                holder.setImageResource(R.id.iv_answer,if (item.answerType > 0) R.mipmap.ic_question_answer_b_s else R.mipmap.ic_question_answer_b)
            }
            2 ->{
                holder.setImageResource(R.id.iv_answer,if (item.answerType > 0) R.mipmap.ic_question_answer_c_s else R.mipmap.ic_question_answer_c)
            }
//            3 ->{
//                holder.setImageResource(R.id.iv_answer,if (item.answerType > 0) R.mipmap.ic_question_answer_d_s else R.mipmap.ic_question_answer_d)
//            }
        }
        // 0 正常 1 对 2错
        when (item.answerType){
            0 ->{
                holder.setBackgroundResource(R.id.tv_option,R.mipmap.ic_question_bg_3)
                    .setImageResource(R.id.iv_answer_bg,R.mipmap.ic_question_answer_bg_c)
                    .setGone(R.id.iv_wrong,true)
                    .setTextColor(R.id.tv_option,Color.parseColor("#FF9424"))
            }
            2 ->{
                holder.setBackgroundResource(R.id.tv_option,R.mipmap.ic_question_bg_1)
                    .setImageResource(R.id.iv_answer_bg,R.mipmap.ic_question_answer_bg_a)
                    .setGone(R.id.iv_wrong,false)
                    .setImageResource(R.id.iv_wrong,R.mipmap.ic_answer_no)
                    .setTextColor(R.id.tv_option,Color.parseColor("#F76348"))
            }
            1 ->{
                holder.setBackgroundResource(R.id.tv_option,R.mipmap.ic_question_bg_2)
                    .setImageResource(R.id.iv_answer_bg,R.mipmap.ic_question_answer_bg_b)
                    .setGone(R.id.iv_wrong,false)
                    .setImageResource(R.id.iv_wrong,R.mipmap.ic_answer_yes)
                    .setTextColor(R.id.tv_option,Color.parseColor("#009E6C"))
            }
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_answer_practice_option, parent)
    }

}