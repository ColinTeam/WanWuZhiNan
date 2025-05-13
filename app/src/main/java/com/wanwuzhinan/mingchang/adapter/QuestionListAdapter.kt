package com.wanwuzhinan.mingchang.adapter

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.QuestionListData


class QuestionListAdapter : BaseQuickAdapter<QuestionListData,QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: QuestionListData?) {
        item?:return
        holder.setText(R.id.tv_name, item.name)
            .setText(R.id.tv_desc, item.desc)
            .setGone(R.id.iv_top, item.typeid.toInt() == 2)
            .setGone(R.id.iv_top_1, item.typeid.toInt() == 1)

        when(position%6){
            0 ->{
                holder.setImageResource(R.id.iv_back,R.mipmap.bg_question_list_0)
                    .setImageResource(R.id.iv_top,R.mipmap.ic_question_list_0)
                    .setImageResource(R.id.iv_top_1,R.mipmap.ic_answer_list_0)
            }
            1 ->{
                holder.setImageResource(R.id.iv_back,R.mipmap.bg_question_list_1)
                    .setImageResource(R.id.iv_top,R.mipmap.ic_question_list_1)
                    .setImageResource(R.id.iv_top_1,R.mipmap.ic_answer_list_1)
            }
            2 ->{
                holder.setImageResource(R.id.iv_back,R.mipmap.bg_question_list_2)
                    .setImageResource(R.id.iv_top,R.mipmap.ic_question_list_2)
                    .setImageResource(R.id.iv_top_1,R.mipmap.ic_answer_list_2)
            }
            3 ->{
                holder.setImageResource(R.id.iv_back,R.mipmap.bg_question_list_3)
                    .setImageResource(R.id.iv_top,R.mipmap.ic_question_list_3)
                    .setImageResource(R.id.iv_top_1,R.mipmap.ic_answer_list_3)
            }
            4 ->{
                holder.setImageResource(R.id.iv_back,R.mipmap.bg_question_list_4)
                    .setImageResource(R.id.iv_top,R.mipmap.ic_question_list_4)
                    .setImageResource(R.id.iv_top_1,R.mipmap.ic_answer_list_4)
            }
            5 ->{
                holder.setImageResource(R.id.iv_back,R.mipmap.bg_question_list_5)
                    .setImageResource(R.id.iv_top,R.mipmap.ic_question_list_5)
                    .setImageResource(R.id.iv_top_1,R.mipmap.ic_answer_list_5)
            }
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_question_list, parent)
    }

}