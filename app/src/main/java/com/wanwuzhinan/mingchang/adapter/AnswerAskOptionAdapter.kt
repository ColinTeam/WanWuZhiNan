package com.wanwuzhinan.mingchang.adapter

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.QuestionListData


class AnswerAskOptionAdapter : BaseQuickAdapter<QuestionListData.AnswerBean,QuickViewHolder>() {

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: QuestionListData.AnswerBean?) {
        holder.setText(R.id.tv_option,"${item!!.key}. ${item.answer}")

        // 0 正常 1 对 2错
        when(item.answerType){
            0 ->{
                holder.setBackgroundResource(R.id.cl,R.mipmap.ic_keju_answer_bg_1)
                    .setImageResource(R.id.iv_wrong,R.mipmap.ic_quan)
                    .setTextColor(R.id.tv_option,Color.parseColor("#FF9424"))
            }
            1 ->{
                holder.setBackgroundResource(R.id.cl,R.mipmap.ic_keju_answer_bg_2)
                    .setImageResource(R.id.iv_wrong,R.mipmap.ic_answer_yes)
                    .setTextColor(R.id.tv_option,Color.parseColor("#009E6C"))
            }
            2 ->{
                holder.setBackgroundResource(R.id.cl,R.mipmap.ic_keju_answer_bg_3)
                    .setImageResource(R.id.iv_wrong,R.mipmap.ic_answer_no)
                    .setTextColor(R.id.tv_option,Color.parseColor("#F76348"))
            }
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_answer_ask_option, parent)
    }

}