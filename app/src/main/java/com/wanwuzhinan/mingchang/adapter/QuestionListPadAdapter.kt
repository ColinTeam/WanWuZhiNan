package com.wanwuzhinan.mingchang.adapter

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.data.QuestionListData


class QuestionListPadAdapter : BaseQuickAdapter<QuestionListData,QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: QuestionListData?) {
        holder.setText(R.id.tv_name,item!!.name)
        holder.setBackgroundResource(R.id.cl,if(item.select) R.drawable.shape_dae3fc_question_select_30 else R.drawable.shape_dae3fc_30)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.item_question_list_pad, parent)
    }

}